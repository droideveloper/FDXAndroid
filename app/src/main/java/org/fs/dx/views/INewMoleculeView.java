package org.fs.dx.views;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;

import org.fs.common.IView;
import org.fs.dx.adapters.ReferenceRecyclerViewAdapter;
import org.fs.dx.component.AppComponent;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.INewMoleculeView
 */
public interface INewMoleculeView extends IView {

    void bindView();
    void bindAdapter(ReferenceRecyclerViewAdapter adapter);
    void setupView();

    void showError(Snackbar snackbar);
    void showUndo(Snackbar undo);
    void hideUndo(Snackbar undo);

    void setTitle(String str);
    void startActivity(Intent intent);
    void finish();
    void putResult(int code);
    void putResult(int code, Intent data);

    void setTextName(String str);
    void setTextPrice(String str);
    void onBackPressed();

    CoordinatorLayout   viewRoot();
    Context             getContext();
    FragmentManager     provideSupportFragmentManager();
    AppComponent        provideAppComponent();

}