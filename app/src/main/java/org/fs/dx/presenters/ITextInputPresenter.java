package org.fs.dx.presenters;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;

import org.fs.common.IPresenter;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.presenters.ITextInputPresenter
 */
public interface ITextInputPresenter extends IPresenter {

    void restoreState(Bundle restoreState);
    void storeState(Bundle storeState);

    View.OnClickListener provideClickListener();
    TextWatcher          provideTextWatcher();
}