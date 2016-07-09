package org.fs.dx.views;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractFragment;
import org.fs.dx.FDXApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.MoleculeRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;
import org.fs.dx.presenters.AddMoleculePresenter;
import org.fs.dx.presenters.IAddMoleculePresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.AddMoleculeView
 */
public class AddMoleculeView extends AbstractFragment<IAddMoleculePresenter> implements IAddMoleculeView {

    private FloatingActionButton addView;
    private RecyclerView         recyclerView;
    private Toolbar              toolbar;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(inflater, container);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.storeState(savedInstanceState != null ? savedInstanceState : getArguments());
        presenter.onCreate();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override public Toolbar getToolbar() {
        return toolbar;
    }

    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public View bindView(LayoutInflater factory, ViewGroup parent) {
        View view = factory.inflate(R.layout.layout_add_molecule_view, parent, false);
        addView = ViewUtility.findViewById(view, R.id.addView);
        toolbar = ViewUtility.findViewById(view, R.id.toolbar);
        recyclerView = ViewUtility.findViewById(view, R.id.recyclerView);
        return view;
    }

    @Override public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResult(requestCode, resultCode, data);
    }

    @Override public void setupView() {
        //add view
        addView.setOnClickListener(presenter.provideViewClickListener());
        //recycler setup
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper touchHelper = presenter.provideRecyclerViewSwipeListener();
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override public void bindAdapter(MoleculeRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override public void showUndo(Snackbar snackbar) {
        snackbar.show();
    }

    @Override public void hideUndo(Snackbar snackbar) {
        if (snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override public void showError(Snackbar error) {
        error.show();
    }

    @Override public void hideError(Snackbar error) {
        if (error.isShown()) {
            error.dismiss();
        }
    }

    @Override public void setTitle(String str) {
        toolbar.setTitle(str);
    }

    @Override public CoordinatorLayout viewRoot() {
        return ViewUtility.castAsField(getView());
    }

    @Override public AppComponent provideAppComponent() {
        Application app = getActivity().getApplication();
        if (app instanceof FDXApplication) {
            FDXApplication fdx = (FDXApplication) app;
            return fdx.getAppComponent();
        }
        return null;
    }

    @Override protected IAddMoleculePresenter presenter() {
        return new AddMoleculePresenter(this);
    }

    @Override public boolean isAvailable() {
        return isCallingSafe();
    }

    @Override protected String getClassTag() {
        return AddMoleculeView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}