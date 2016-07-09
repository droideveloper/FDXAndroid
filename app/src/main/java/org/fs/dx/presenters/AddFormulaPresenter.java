package org.fs.dx.presenters;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.FormulaRecyclerViewAdapter;
import org.fs.dx.adapters.ResultRecyclerViewAdapter;
import org.fs.dx.commons.OnFormulaSelectedListener;
import org.fs.dx.component.AppComponent;
import org.fs.dx.entities.Element;
import org.fs.dx.entities.Molecule;
import org.fs.dx.entities.Reference;
import org.fs.dx.events.ToolbarEvent;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.dx.views.IAddFormulaView;
import org.fs.dx.views.ISelectMoleculeView;
import org.fs.dx.views.SelectMoleculeView;
import org.fs.util.Collections;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.AddFormulaPresenter
 */
public class AddFormulaPresenter extends AbstractPresenter<IAddFormulaView> implements IAddFormulaPresenter,
                                                                                       View.OnClickListener,
                                                                                       OnFormulaSelectedListener,
                                                                                       ISelectMoleculeView.Callback {

    private final static String TAG_SELECT_MOLECULE = "SELECT_MOLECULE_VIEW";
    private final static String TOTAL_STR           = "TOTAL";

    final BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {
            //no op
        }

        @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //no op
        }
    };

    final ItemTouchHelper.SimpleCallback swipeListener = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {

        @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //no op
            return false;
        }

        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (view.isAvailable()) {
                if (formulaRecyclerAdapter != null) {
                    final int position = viewHolder.getAdapterPosition();
                    final Pair<Molecule, Double> pair = formulaRecyclerAdapter.removeAt(position);
                    calcResults();//calculate
                    if (pair != null) {
                        String str = String.format(Locale.getDefault(), view.getContext().getString(R.string.undo_molecule_text), pair.first.getName());
                        SpannableString strTitle = ChemistryUtility.toChemistryText(str);
                        SpannableString strAction = new SpannableString(view.getContext().getString(R.string.undo));
                        strAction.setSpan(new ImageSpan(view.getContext(), R.drawable.ic_undo_white_24dp),
                                          strAction.length() - 1,
                                          strAction.length(),
                                          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        final Snackbar swipeShow = Snackbar.make(view.viewRoot(), strTitle, Snackbar.LENGTH_LONG);
                        swipeShow.setAction(strAction, new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                if (view.isAvailable()) {
                                    view.hideUndo(swipeShow);
                                    formulaRecyclerAdapter.appendAt(pair, position);
                                    //calculate
                                    calcResults();
                                }
                            }
                        });
                        view.showUndo(swipeShow);
                    }
                }
            }
        }
    };

    private ItemTouchHelper             touchHelper;
    private FormulaRecyclerViewAdapter  formulaRecyclerAdapter;
    private ResultRecyclerViewAdapter   resultRecyclerAdapter;

    private boolean create;
    private int     index;

    @Inject IDatabaseManager dbManager;

    public AddFormulaPresenter(IAddFormulaView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        //no op
    }

    @Override public void storeState(Bundle storeState) {
        //no op
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.clearView: {
                if (resultRecyclerAdapter != null) {
                    resultRecyclerAdapter.clear();
                }
                if (formulaRecyclerAdapter != null) {
                    formulaRecyclerAdapter.clear();
                }
                return true;
            }
            default: break;
        }
        return false;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater factory) {
        factory.inflate(R.menu.formula_menu, menu);
    }

    @Override
    public ItemTouchHelper provideRecyclerViewSwipeListener() {
        if (touchHelper == null) {
            touchHelper = new ItemTouchHelper(swipeListener);
        }
        return touchHelper;
    }

    @Override public BottomSheetBehavior.BottomSheetCallback provideBottomSheetCallback() {
        return bottomSheetCallback;
    }

    @Override public View.OnClickListener provideViewClickListener() {
        return this;
    }

    @Override public void onCreate() {
        if (view.isAvailable()) {
            view.setupView();
            String titleStr = view.getContext().getString(R.string.formulas_title);
            view.setTitle(titleStr);
            AppComponent appComponent = view.provideAppComponent();
            if (appComponent != null) {
                appComponent.inject(this);
            }
        }
    }

    @Override public void onStart() {
        if (view.isAvailable()) {
            if (formulaRecyclerAdapter == null) {
                formulaRecyclerAdapter = new FormulaRecyclerViewAdapter(view.getContext());
                formulaRecyclerAdapter.setSelectedListener(this);
                view.bindAdapter(formulaRecyclerAdapter);
            }
            if (resultRecyclerAdapter == null) {
                resultRecyclerAdapter = new ResultRecyclerViewAdapter(view.getContext());
                view.bindAdapter(resultRecyclerAdapter);
            }
            BusManager.send(new ToolbarEvent(view.getToolbar()));
        }
    }

    @Override public void onStop() {
        //no op
    }

    @Override public void onDestroy() {
        //no op
    }

    @Override public void onClick(View v) {
        if (view.isAvailable()) {
            switch (v.getId()) {
                case R.id.addView: {
                    create = true;
                    SelectMoleculeView selectMoleculeView = new SelectMoleculeView();
                    selectMoleculeView.setDatabaseManager(dbManager);
                    selectMoleculeView.setCallback(this);

                    FragmentManager fragmentManager = view.provideFragmentManager();
                    selectMoleculeView.show(fragmentManager, TAG_SELECT_MOLECULE);
                    break;
                }
                case R.id.showView: {
                    if (!view.isSheetShown()) {
                        view.showSheet();
                        Resources res = view.getContext().getResources();
                        view.setPeekHeight(res.getDimensionPixelSize(R.dimen.peek_height));
                    } else {
                        view.setPeekHeight(0);
                        view.hideSheet();
                    }
                    break;
                }
            }
        }
    }

    @Override public void onSelected(Pair<Molecule, Double> selected) {
        if (view.isAvailable()) {
            if (formulaRecyclerAdapter != null) {

                index = formulaRecyclerAdapter.positionOf(selected);
                create = false;

                SelectMoleculeView selectMoleculeView = new SelectMoleculeView();
                Bundle args = new Bundle();
                args.putParcelable(SelectMoleculePresenter.KEY_MOL, selected.first);
                args.putString(SelectMoleculePresenter.KEY_STR, String.valueOf(selected.second));
                selectMoleculeView.setArguments(args);

                selectMoleculeView.setDatabaseManager(dbManager);
                selectMoleculeView.setCallback(this);

                FragmentManager fragmentManager = view.provideFragmentManager();
                selectMoleculeView.show(fragmentManager, TAG_SELECT_MOLECULE);
            }
        }
    }

    @Override protected String getClassTag() {
        return AddFormulaPresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onSuccess(Molecule molecule, double amount) {
        if (view.isAvailable()) {
            if (formulaRecyclerAdapter != null) {
                if (create) {
                    formulaRecyclerAdapter.appendData(new Pair<>(molecule, amount), false);
                } else {
                    formulaRecyclerAdapter.setAt(new Pair<>(molecule, amount), index);
                }
                calcResults();
            }
        }
    }

    void calcResults() {
        if (view.isAvailable()) {
            if (formulaRecyclerAdapter != null) {
                List<Pair<Molecule, Double>> dataSet = formulaRecyclerAdapter.provideClone();
                if (!Collections.isNullOrEmpty(dataSet)) {
                    Map<String, Double> map = new HashMap<>();
                    for (Pair<Molecule, Double> pair : dataSet) {
                        try {
                            List<Reference> references = dbManager.queryFor(pair.first);
                            if (!Collections.isNullOrEmpty(references)) {
                                for (Reference ref : references) {
                                    Element element = dbManager.getElement(ref.getElementId());
                                    if (map.containsKey(element.getName())) {
                                        double current = map.get(element.getName());
                                        current += toPercent(ref.getPercentage()) * pair.second;
                                        map.put(element.getName(), current);
                                    } else {
                                        map.put(element.getName(), toPercent(ref.getPercentage()) * pair.second);
                                    }
                                }
                            }
                        } catch (SQLException sql) {
                            sql.printStackTrace();
                        }
                        if (map.containsKey(TOTAL_STR)) {
                            double current = map.get(TOTAL_STR);
                            current += toPercent(pair.second) * pair.first.getPrice();
                            map.put(TOTAL_STR, current);
                        } else {
                            map.put(TOTAL_STR, toPercent(pair.second) * pair.first.getPrice());
                        }
                    }
                    //update ui according to data
                    if (!map.isEmpty()) {
                        if (resultRecyclerAdapter != null) {
                            resultRecyclerAdapter.update(map);
                        }
                        Resources res = view.getContext().getResources();
                        view.setPeekHeight(res.getDimensionPixelSize(R.dimen.peek_height));
                    } else {
                        view.setPeekHeight(0);
                    }
                } else {
                    //hide view accordingly
                    view.setPeekHeight(0);
                }
            }
        }
    }

    double toPercent(double value) {
        return value / 100d;
    }

    @Override public void onCancel() {
        //do nothing
    }
}