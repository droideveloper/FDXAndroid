package org.fs.dx.views;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.common.IView;
import org.fs.dx.adapters.MoleculeRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.IAddMoleculeView
 */
public interface IAddMoleculeView extends IView {

    void setupView();
    View bindView(LayoutInflater factory, ViewGroup parent);
    void bindAdapter(MoleculeRecyclerViewAdapter adapter);

    void showUndo(Snackbar snackbar);
    void hideUndo(Snackbar snackbar);

    void showError(Snackbar error);
    void hideError(Snackbar error);

    void startActivityForResult(Intent intent, int requestCode);
    void setTitle(String str);

    Toolbar             getToolbar();
    CoordinatorLayout   viewRoot();
    Context             getContext();
    AppComponent        provideAppComponent();
    boolean             isAvailable();
}
