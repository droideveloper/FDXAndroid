package org.fs.dx.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.presenters.IAddMoleculePresenter
 */
public interface IAddMoleculePresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);
    void activityResult(int requestCode, int resultCode, Intent data);

    ItemTouchHelper                 provideRecyclerViewSwipeListener();
    View.OnClickListener            provideViewClickListener();
}