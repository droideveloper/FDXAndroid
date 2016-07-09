package org.fs.dx.views;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.common.IView;
import org.fs.dx.adapters.ElementRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.views.IAddElementView
 */
public interface IAddElementView extends IView {

    View bindView(LayoutInflater factory, ViewGroup parent);
    void setupView();
    void bindAdapter(ElementRecyclerViewAdapter adapter);

    void showUndo(Snackbar snackbar);
    void hideUndo(Snackbar snackbar);

    void showError(Snackbar snackbar);
    void hideError(Snackbar snackbar);

    void setTitle(String str);

    Toolbar           getToolbar();
    CoordinatorLayout viewRoot();
    Context           getContext();
    FragmentManager   provideFragmentManager();
    AppComponent      provideAppComponent();
    boolean           isAvailable();
}