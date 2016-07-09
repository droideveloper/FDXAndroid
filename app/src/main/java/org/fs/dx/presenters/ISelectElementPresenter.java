package org.fs.dx.presenters;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Spinner;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.ISelectElementPresenter
 */
public interface ISelectElementPresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);

    Spinner.OnItemSelectedListener provideSpinnerSelectionListener();
    TextWatcher                    provideTextWatcher();
    View.OnClickListener           provideViewClickListener();
}