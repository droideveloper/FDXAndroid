package org.fs.dx.views;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractFragment;
import org.fs.dx.FDXApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.ElementRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;
import org.fs.dx.presenters.AddElementPresenter;
import org.fs.dx.presenters.IAddElementPresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.AddElementView
 */
public class AddElementView extends AbstractFragment<IAddElementPresenter> implements IAddElementView {

    private Toolbar                 toolbar;
    private RecyclerView            recyclerView;
    private FloatingActionButton    addView;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(inflater, container);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //restore state
        presenter.restoreState(savedInstanceState != null ? savedInstanceState : getArguments());
        //create callback
        presenter.onCreate();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.storeState(outState);
    }

    @Override public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public View bindView(LayoutInflater factory, ViewGroup parent) {
        View view = factory.inflate(R.layout.layout_add_element_view, parent, false);
        toolbar = ViewUtility.findViewById(view, R.id.toolbar);
        recyclerView = ViewUtility.findViewById(view, R.id.recyclerView);
        addView = ViewUtility.findViewById(view, R.id.addView);
        return view;
    }

    @Override public void setupView() {
        //setup recycler
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper touchHelper = presenter.provideRecyclerViewSwipeListener();
        touchHelper.attachToRecyclerView(recyclerView);
        //setup add
        addView.setOnClickListener(presenter.provideViewClickListener());
    }

    @Override public void bindAdapter(ElementRecyclerViewAdapter adapter) {
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

    @Override public Toolbar getToolbar() {
        return toolbar;
    }

    @Override public CoordinatorLayout viewRoot() {
        return ViewUtility.castAsField(getView());
    }

    @Override public void showError(Snackbar snackbar) {
        snackbar.show();
    }

    @Override public void hideError(Snackbar snackbar) {
        if (snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override public AppComponent provideAppComponent() {
        Application app = getActivity().getApplication();
        if (app instanceof FDXApplication) {
            FDXApplication fdx = (FDXApplication) app;
            return fdx.getAppComponent();
        }
        return null;
    }

    @Override public void setTitle(String str) {
        toolbar.setTitle(str);
    }

    @Override public boolean isAvailable() {
        return isCallingSafe();
    }

    @Override public FragmentManager provideFragmentManager() {
        return getChildFragmentManager();
    }

    @Override protected IAddElementPresenter presenter() {
        return new AddElementPresenter(this);
    }

    @Override protected String getClassTag() {
        return AddElementView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}