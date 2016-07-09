package org.fs.dx.presenters;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.fs.common.AbstractPresenter;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.views.ITextInputView;
import org.fs.util.StringUtility;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.presenters.TextInputPresenter
 */
public class TextInputPresenter extends AbstractPresenter<ITextInputView> implements ITextInputPresenter,
                                                                                     View.OnClickListener,
                                                                                     TextWatcher {
    public final static String KEY_STR = "bundle.str";

    private String str;

    public TextInputPresenter(ITextInputView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        if (restoreState != null) {
            str = restoreState.getString(KEY_STR);
        }
    }

    @Override public void storeState(Bundle storeState) {
        if (str != null) {
            storeState.putString(KEY_STR, str);
        }
    }

    @Override public View.OnClickListener provideClickListener() {
        return this;
    }

    @Override public TextWatcher provideTextWatcher() {
        return this;
    }

    @Override public void onCreate() {
        if (view.isAvailable()) {
            view.setupView();
        }
    }

    @Override public void onStart() {
        if (view.isAvailable()) {
            if (str != null) {
                view.setStr(str);
            }
            final Dialog dialog = view.getDialog();
            if (dialog != null) {
                final Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                     ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        }
    }

    @Override public void onStop() {
        //no op
    }

    @Override public void onDestroy() {
       //no op
    }

    @Override protected String getClassTag() {
        return TextInputPresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onClick(View btn) {
        if (view.isAvailable()) {
            final int id = btn.getId();
            final ITextInputView.Callback callback = view.getCallback();
            switch (id) {
                case R.id.btnOk: {
                    if (StringUtility.isNullOrEmpty(str)) {
                        String error = view.getContext().getString(R.string.new_element_empty_text_error);
                        view.showError(error);
                    } else {
                        if (callback != null) {
                            callback.onSuccess(str);
                        }
                        view.dismiss();
                    }
                    break;
                }
                case R.id.btnCancel: {
                    if (callback != null) {
                        callback.onCancel();
                    }
                    view.dismiss();
                    break;
                }
            }
        }
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //no op
    }

    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //no op
    }

    @Override public void afterTextChanged(Editable editable) {
        this.str = editable.toString();
        if (!StringUtility.isNullOrEmpty(str)) {
            if (view.isAvailable() && view.isErrorShown()) {
                view.hideError();//if we show error it will be hidden
            }
        }
    }
}