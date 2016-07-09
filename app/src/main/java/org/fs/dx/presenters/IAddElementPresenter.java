package org.fs.dx.presenters;

import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.presenters.IAddElementPresenter
 */
public interface IAddElementPresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);

    ItemTouchHelper                 provideRecyclerViewSwipeListener();
    View.OnClickListener            provideViewClickListener();
}