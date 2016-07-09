package org.fs.dx.presenters;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.IAddFormulaPresenter
 */
public interface IAddFormulaPresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);
    boolean onOptionsItemSelected(MenuItem item);
    void onCreateOptionsMenu(Menu menu, MenuInflater inflater);

    ItemTouchHelper                             provideRecyclerViewSwipeListener();
    BottomSheetBehavior.BottomSheetCallback     provideBottomSheetCallback();
    View.OnClickListener                        provideViewClickListener();

}