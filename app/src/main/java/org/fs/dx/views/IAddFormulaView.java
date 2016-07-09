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
import org.fs.dx.adapters.FormulaRecyclerViewAdapter;
import org.fs.dx.adapters.ResultRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.IAddFormulaView
 */
public interface IAddFormulaView extends IView {

    View bindView(LayoutInflater factory, ViewGroup parent);
    void bindAdapter(ResultRecyclerViewAdapter recyclerAdapter);
    void bindAdapter(FormulaRecyclerViewAdapter recyclerAdapter);
    void setupView();

    void setTitle(String str);

    void showError(Snackbar error);
    void showUndo(Snackbar undo);
    void hideUndo(Snackbar undo);
    void setPeekHeight(int peek);
    void hideSheet();
    void showSheet();

    boolean             isSheetShown();
    Toolbar             getToolbar();
    Context             getContext();
    CoordinatorLayout   viewRoot();
    FragmentManager     provideFragmentManager();
    AppComponent        provideAppComponent();
    boolean             isAvailable();
}