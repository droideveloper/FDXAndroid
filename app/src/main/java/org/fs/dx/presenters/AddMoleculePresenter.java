package org.fs.dx.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;

import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.MoleculeRecyclerViewAdapter;
import org.fs.dx.commons.OnMoleculeSelectedListener;
import org.fs.dx.component.AppComponent;
import org.fs.dx.entities.Molecule;
import org.fs.dx.events.ToolbarEvent;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.dx.usecases.QueryMoleculesUseCase;
import org.fs.dx.usecases.QueryMoleculesUseCaseImp;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.dx.views.IAddMoleculeView;
import org.fs.dx.views.NewMoleculeView;
import org.fs.util.Collections;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.presenters.AddMoleculePresenter
 */
public class AddMoleculePresenter extends AbstractPresenter<IAddMoleculeView> implements IAddMoleculePresenter,
                                                                                         OnMoleculeSelectedListener,
                                                                                         QueryMoleculesUseCase.Callback {


    private final static int REQUEST_CODE = 0x09;

    final View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if(view.isAvailable()) {
                Intent intent = new Intent(view.getContext(), NewMoleculeView.class);
                view.startActivityForResult(intent, REQUEST_CODE);
            }
        }
    };

    final ItemTouchHelper.SimpleCallback recyclerSwipeListener = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
        @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        //TODO add option of retrieve of old
        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (view.isAvailable()) {
                if (recyclerAdapter != null) {
                    final int position = viewHolder.getAdapterPosition();
                    final Molecule molecule = recyclerAdapter.removeAt(position);
                    if (molecule != null) {
                        String str = String.format(Locale.getDefault(), view.getContext().getString(R.string.undo_molecule_text), molecule.getName());
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
                                    molecule.setId(null);
                                    dbManager.add(molecule);//store
                                    recyclerAdapter.appendAt(molecule, position);
                                }
                            }
                        });
                        view.showUndo(swipeShow);
                        dbManager.removeReference(molecule);//remove previous reference
                        dbManager.remove(molecule);//remove
                    }
                }
            }
        }
    };

    private MoleculeRecyclerViewAdapter recyclerAdapter;
    private ItemTouchHelper             touchHelper;

    @Inject IDatabaseManager dbManager;

    public AddMoleculePresenter(IAddMoleculeView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        //restore state ?
    }

    @Override public void storeState(Bundle storeState) {
        //no restore state?
    }

    @Override public ItemTouchHelper provideRecyclerViewSwipeListener() {
        if (touchHelper == null) {
            touchHelper = new ItemTouchHelper(recyclerSwipeListener);
        }
        return touchHelper;
    }

    @Override public View.OnClickListener provideViewClickListener() {
        return addOnClickListener;
    }

    @Override public void onCreate() {
        if (view.isAvailable()) {
            view.setupView();
            String str = view.getContext().getString(R.string.molecules_title);
            view.setTitle(str);
            AppComponent appComponent = view.provideAppComponent();
            if (appComponent != null) {
                appComponent.inject(this);
            }
        }
    }

    @Override public void onStart() {
        if (view.isAvailable()) {
            if (recyclerAdapter == null) {
                recyclerAdapter = new MoleculeRecyclerViewAdapter(view.getContext());
                recyclerAdapter.setSelectedListener(this);
                view.bindAdapter(recyclerAdapter);
                //
                QueryMoleculesUseCase usecase = new QueryMoleculesUseCaseImp();
                usecase.setDatabaseManager(dbManager);
                usecase.executeAsync(this);
            }
            BusManager.send(new ToolbarEvent(view.getToolbar()));
        }
    }

    @Override public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == NewMoleculePresenter.RESULT_OK) {
                boolean create = data.getBooleanExtra(NewMoleculePresenter.KEY_CREATE, false);
                Molecule molecule = data.getParcelableExtra(NewMoleculePresenter.KEY_MOL);
                if (create) {
                    if (recyclerAdapter != null && molecule != null) {
                        recyclerAdapter.appendData(molecule, false);
                    }
                } else {
                    int position = recyclerAdapter.positionOf(molecule);
                    if (position != -1) {
                        recyclerAdapter.notifyItemChanged(position);
                    }
                }
            }
        }
    }

    @Override public void onStop() {
        //no-op
    }

    @Override public void onDestroy() {
        //no-op
    }

    @Override protected String getClassTag() {
        return AddMoleculePresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onSelected(Molecule molecule) {
        //BusManager.send(new ModifyMoleculeEvent(molecule)); modify
        Intent intent = new Intent(view.getContext(), NewMoleculeView.class);
        intent.putExtra(NewMoleculePresenter.KEY_MOL, molecule);
        view.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override public void onSuccess(List<Molecule> dataSet) {
        if (!Collections.isNullOrEmpty(dataSet)) {
            if (view.isAvailable()) {
                recyclerAdapter.appendData(dataSet, true);
            }
        }
    }

    @Override public void onError(Throwable tr) {
        if (view.isAvailable()) {
            StringWriter str = new StringWriter(128);
            PrintWriter ptr = new PrintWriter(str);
            tr.printStackTrace(ptr);
            Snackbar error = Snackbar.make(view.viewRoot(), str.toString(), Snackbar.LENGTH_LONG);
            view.showError(error);
        }
    }
}