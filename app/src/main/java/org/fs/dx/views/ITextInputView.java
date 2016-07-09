package org.fs.dx.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.common.IView;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.ITextInputView
 */
public interface ITextInputView extends IView {

    View bindView(LayoutInflater factory, ViewGroup parent);
    void setupView();

    void setCallback(Callback callback);

    void showError(String error);
    void hideError();
    void dismiss();
    void setStr(String str);

    boolean  isErrorShown();
    boolean  isAvailable();
    Context  getContext();
    Dialog   getDialog();
    Callback getCallback();

    interface Callback {
        void onSuccess(String str);
        void onCancel();
    }
}