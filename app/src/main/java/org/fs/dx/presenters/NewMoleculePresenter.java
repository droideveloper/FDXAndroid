package org.fs.dx.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;

import org.fs.common.AbstractPresenter;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.ReferenceRecyclerViewAdapter;
import org.fs.dx.commons.OnReferenceSelectedListener;
import org.fs.dx.component.AppComponent;
import org.fs.dx.entities.Element;
import org.fs.dx.entities.Molecule;
import org.fs.dx.entities.Reference;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.dx.usecases.QueryReferencesUseCase;
import org.fs.dx.usecases.QueryReferencesUseCaseImp;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.dx.views.INewMoleculeView;
import org.fs.dx.views.INumberInputView;
import org.fs.dx.views.ISelectElementView;
import org.fs.dx.views.ITextInputView;
import org.fs.dx.views.NumberInputView;
import org.fs.dx.views.SelectElementView;
import org.fs.dx.views.TextInputView;
import org.fs.util.Collections;
import org.fs.util.StringUtility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.NewMoleculePresenter
 */
public class NewMoleculePresenter extends AbstractPresenter<INewMoleculeView> implements INewMoleculePresenter,
                                                                                         OnReferenceSelectedListener,
                                                                                         QueryReferencesUseCase.Callback {

    public final static String KEY_MOL    = "bundle.molecule";
    public final static String KEY_CREATE = "bundle.create";

    private final static String TAG_TEXT_INPUT_VIEW         = "TEXT_INPUT_VIEW";
    private final static String TAG_NUMB_INPUT_VIEW         = "NUMB_INPUT_VIEW";
    private final static String TAG_SELECTED_ELEMENT_VIEW   = "SELECTED_ELEMENT_VIEW";


    public final static int RESULT_CANCEL = 0x01;
    public final static int RESULT_OK     = 0x02;

    final ITextInputView.Callback textCallback = new ITextInputView.Callback() {
        @Override public void onSuccess(String str) {
            view.setTextName(str);
            moleculeSelected.setName(str);
            dbManager.update(moleculeSelected);
        }

        @Override public void onCancel() {
            //no op
        }
    };

    final INumberInputView.Callback numberCallback = new INumberInputView.Callback() {
        @Override public void onSuccess(String str) {
            view.setTextPrice(str);
            double price = Double.parseDouble(str);
            moleculeSelected.setPrice(price);
            dbManager.update(moleculeSelected);
        }

        @Override public void onCancel() {
            //no op
        }
    };

    final ISelectElementView.Callback selectedElementCallback = new ISelectElementView.Callback() {
        @Override public void onSuccess(Reference reference) {
            if (reference.getId() == null) {
                reference.setMoleculeId(moleculeSelected.getId());
                dbManager.add(reference);
            } else {
                dbManager.update(reference);
            }
            if(recyclerAdapter != null) {
                recyclerAdapter.appendData(reference, false);
            }
        }

        @Override public void onCancel() {
            //no op
        }
    };

    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                //read and write name
                case R.id.txtNameView: {
                    TextInputView textInputView = new TextInputView();
                    if (!StringUtility.isNullOrEmpty(moleculeSelected.getName())) {
                        Bundle args = new Bundle();
                        args.putString(TextInputPresenter.KEY_STR, moleculeSelected.getName());
                        textInputView.setArguments(args);
                    }
                    textInputView.setCallback(textCallback);
                    FragmentManager fragmentManager = view.provideSupportFragmentManager();
                    textInputView.show(fragmentManager, TAG_TEXT_INPUT_VIEW);
                    break;
                }
                //read and write price
                case R.id.txtPriceView: {
                    NumberInputView numberInputView = new NumberInputView();
                    Bundle args = new Bundle();
                    args.putString(NumberInputPresenter.KEY_STR, String.valueOf(moleculeSelected.getPrice()));
                    numberInputView.setArguments(args);
                    numberInputView.setCallback(numberCallback);
                    FragmentManager fragmentManager = view.provideSupportFragmentManager();
                    numberInputView.show(fragmentManager, TAG_NUMB_INPUT_VIEW);
                    break;
                }
                //add
                case R.id.addView: {
                    SelectElementView selectElementView = new SelectElementView();
                    selectElementView.setCallback(selectedElementCallback);
                    selectElementView.setDatabaseManager(dbManager);
                    FragmentManager fragmentManager = view.provideSupportFragmentManager();
                    selectElementView.show(fragmentManager, TAG_SELECTED_ELEMENT_VIEW);
                    break;
                }

                case R.id.saveView: {
                    ok();
                    break;
                }
            }
        }
    };

    final Toolbar.OnClickListener navigationClickListener = new Toolbar.OnClickListener() {
        @Override public void onClick(View v) {
            cancel();
        }
    };

    final ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
        @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //no op
            return false;
        }

        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (recyclerAdapter != null) {
                final int position = viewHolder.getAdapterPosition();
                final Reference reference = recyclerAdapter.removeAt(position);
                if (reference != null) {
                    Element element = dbManager.getElement(reference.getElementId());//read text
                    String str = String.format(Locale.getDefault(), view.getContext().getString(R.string.undo_molecule_text), element.getName());
                    SpannableString strTitle = ChemistryUtility.toChemistryText(str);
                    SpannableString strAction = new SpannableString(view.getContext().getString(R.string.undo));
                    strAction.setSpan(new ImageSpan(view.getContext(), R.drawable.ic_undo_white_24dp),
                                      strAction.length() - 1,
                                      strAction.length(),
                                      Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    final Snackbar swipeShow = Snackbar.make(view.viewRoot(), strTitle, Snackbar.LENGTH_LONG);
                    swipeShow.setAction(strAction, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                                view.hideUndo(swipeShow);
                                recyclerAdapter.appendAt(reference, position);
                                dbManager.add(reference);//store

                        }
                    });
                    view.showUndo(swipeShow);
                    dbManager.remove(reference);//remove
                }

            }
        }
    };

    private Molecule                     moleculeSelected;

    private ItemTouchHelper              touchHelper;
    private ReferenceRecyclerViewAdapter recyclerAdapter;

    private boolean create;

    @Inject IDatabaseManager             dbManager;

    public NewMoleculePresenter(INewMoleculeView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        if (restoreState != null) {
            moleculeSelected = restoreState.getParcelable(KEY_MOL);
        }
    }

    @Override public void storeState(Bundle storeState) {
        if (moleculeSelected != null) {
            storeState.putParcelable(KEY_MOL, moleculeSelected);
        }
    }

    @Override public void onBackPressed() {
        cancel();
    }

    @Override public View.OnClickListener provideViewClickListener() {
        return clickListener;
    }

    @Override public ItemTouchHelper provideItemTouchHelper() {
        if (touchHelper == null) {
            touchHelper = new ItemTouchHelper(swipeCallback);
        }
        return touchHelper;
    }

    @Override public Toolbar.OnClickListener provideNavigationClickListener() {
        return navigationClickListener;
    }

    @Override public void onCreate() {
        view.setupView();
        AppComponent component = view.provideAppComponent();
        component.inject(this);
        if (moleculeSelected != null) {
            view.setTitle(moleculeSelected.getName());
        }
    }

    @Override public void onStart() {
        if (recyclerAdapter == null) {
            recyclerAdapter = new ReferenceRecyclerViewAdapter(view.getContext());
            recyclerAdapter.setDbManager(dbManager);
            recyclerAdapter.setSelectedListener(this);
            view.bindAdapter(recyclerAdapter);
            if (moleculeSelected != null) {
                QueryReferencesUseCase usecase = new QueryReferencesUseCaseImp();
                usecase.setDatabaseManager(dbManager);
                usecase.setMolecule(moleculeSelected);
                usecase.executeAsync(this);

                view.setTextName(moleculeSelected.getName());
                view.setTextPrice(String.valueOf(moleculeSelected.getPrice()));

            } else {
                moleculeSelected = new Molecule();
                create = true;
                //store db
                dbManager.add(moleculeSelected);
            }
        }
    }

    void cancel() {
        if (create) {
            try {
                List<Reference> referenceList = dbManager.queryFor(moleculeSelected);
                if (!Collections.isNullOrEmpty(referenceList)) {
                    for (Reference ref : referenceList) {
                        dbManager.remove(ref);
                    }
                }
                dbManager.remove(moleculeSelected);
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
        view.putResult(RESULT_CANCEL);
        view.finish();
    }

    void ok() {
        Intent data = new Intent();
        data.putExtra(KEY_MOL, moleculeSelected);
        data.putExtra(KEY_CREATE, create);
        view.putResult(RESULT_OK, data);
        view.finish();
    }

    @Override public void onStop() {
        //no op
    }

    @Override public void onDestroy() {
        //no op
    }

    @Override public void onSelected(Reference reference) {
        SelectElementView selectElementView = new SelectElementView();
        selectElementView.setCallback(selectedElementCallback);
        selectElementView.setDatabaseManager(dbManager);
        FragmentManager fragmentManager = view.provideSupportFragmentManager();

        Element element = dbManager.getElement(reference.getElementId());
        Bundle args = new Bundle();
        args.putParcelable(SelectElementPresenter.KEY_REF, reference);
        args.putParcelable(SelectElementPresenter.KEY_ELM, element);
        args.putString(SelectElementPresenter.KEY_STR, String.valueOf(reference.getPercentage()));
        selectElementView.setArguments(args);//set args

        selectElementView.show(fragmentManager, TAG_SELECTED_ELEMENT_VIEW);
    }

    @Override public void onSuccess(List<Reference> references) {
        if (recyclerAdapter != null && !Collections.isNullOrEmpty(references)) {
            recyclerAdapter.appendData(references, true);
        }
    }

    @Override public void onError(Throwable tr) {
        StringWriter str = new StringWriter(128);
        PrintWriter  ptr = new PrintWriter(str);
        tr.printStackTrace(ptr);
        Snackbar error = Snackbar.make(view.viewRoot(), str.toString(), Snackbar.LENGTH_LONG);
        view.showError(error);
    }

    @Override protected String getClassTag() {
        return NewMoleculePresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}