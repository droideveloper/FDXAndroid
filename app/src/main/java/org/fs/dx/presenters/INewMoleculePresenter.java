package org.fs.dx.presenters;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.INewMoleculePresenter
 */
public interface INewMoleculePresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);

    void onBackPressed();

    View.OnClickListener    provideViewClickListener();
    ItemTouchHelper         provideItemTouchHelper();
    Toolbar.OnClickListener provideNavigationClickListener();
}