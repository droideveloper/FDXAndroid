package org.fs.dx.views;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractActivity;
import org.fs.core.AbstractApplication;
import org.fs.dx.FDXApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.ReferenceRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;
import org.fs.dx.presenters.INewMoleculePresenter;
import org.fs.dx.presenters.NewMoleculePresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.NewMoleculeView
 */
public class NewMoleculeView extends AbstractActivity<INewMoleculePresenter> implements INewMoleculeView {

    private CoordinatorLayout       coordinatorLayout;
    private Toolbar                 toolbar;
    private View                    txtNameView;
    private TextView                txtNameInput;
    private View                    txtPriceView;
    private TextView                txtPriceInput;
    private RecyclerView            recyclerView;
    private FloatingActionButton    addView;
    private FloatingActionButton    saveView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we can only grab it this way
        overridePendingTransition(R.anim.translate_in_right, R.anim.scale_out);
        setContentView(R.layout.layout_new_molecule_view);
        bindView();
        presenter.restoreState(savedInstanceState != null ? savedInstanceState : getIntent().getExtras());
        presenter.onCreate();
    }

    @Override protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void bindView() {
        coordinatorLayout = ViewUtility.findViewById(this, R.id.containerLayout);
        toolbar = ViewUtility.findViewById(this, R.id.toolbar);
        txtNameView = findViewById(R.id.txtNameView);
        txtNameInput = ViewUtility.findViewById(this, R.id.txtNameInput);
        txtPriceView = findViewById(R.id.txtPriceView);
        txtPriceInput = ViewUtility.findViewById(this, R.id.txtPriceInput);
        recyclerView = ViewUtility.findViewById(this, R.id.recyclerView);
        addView = ViewUtility.findViewById(this, R.id.addView);
        saveView = ViewUtility.findViewById(this, R.id.saveView);
        //set it as actionBar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override public void bindAdapter(ReferenceRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override public void setupView() {
        //recycler
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper touchHelper = presenter.provideItemTouchHelper();
        touchHelper.attachToRecyclerView(recyclerView);
        //click
        txtPriceView.setOnClickListener(presenter.provideViewClickListener());
        txtNameView.setOnClickListener(presenter.provideViewClickListener());
        addView.setOnClickListener(presenter.provideViewClickListener());
        saveView.setOnClickListener(presenter.provideViewClickListener());
        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(presenter.provideNavigationClickListener());
    }

    @Override public CoordinatorLayout viewRoot() {
        return coordinatorLayout;
    }

    @Override public void showError(Snackbar snackbar) {
        snackbar.show();
    }

    @Override public void showUndo(Snackbar undo) {
        undo.show();
    }

    @Override public void hideUndo(Snackbar undo) {
        if (undo.isShown()) {
            undo.dismiss();
        }
    }

    @Override public void setTitle(String str) {
        super.setTitle(str);
    }

    @Override public void setTextName(String str) {
        txtNameInput.setText(str);
    }

    @Override public void setTextPrice(String str) {
        txtPriceInput.setText(str);
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.translate_in_right, R.anim.scale_out);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.scale_in, R.anim.translate_out_right);
    }

    @Override public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override public void putResult(int code) {
        setResult(code);
    }

    @Override public void putResult(int code, Intent data) {
        setResult(code, data);
    }

    @Override public Context getContext() {
        return this;
    }

    @Override protected INewMoleculePresenter presenter() {
        return new NewMoleculePresenter(this);
    }

    @Override public FragmentManager provideSupportFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override public AppComponent provideAppComponent() {
        Application app = getApplication();
        if (app instanceof FDXApplication) {
            FDXApplication fdx = (FDXApplication) app;
            return fdx.getAppComponent();
        }
        return null;
    }

    @Override protected String getClassTag() {
        return NewMoleculeView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}