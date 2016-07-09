package org.fs.dx.views;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.common.IView;
import org.fs.dx.entities.Element;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.INewElementView
 */
public interface INewElementView extends IView {

    void setupView();
    View bindView(LayoutInflater factory, ViewGroup parent);
    void setElement(Element element);
    void setCallback(Callback callback);
    void setText(SpannableString str);
    void dismiss();

    Context  getContext();
    Dialog   getDialog();
    boolean  isAvailable();
    Callback getCallback();
    Element  getElement();
    String   getText();


    void showError(String str);
    void hideError();

    interface Callback {
        void onSaveOrUpdate(Element element);
        void onCancel();
    }
}