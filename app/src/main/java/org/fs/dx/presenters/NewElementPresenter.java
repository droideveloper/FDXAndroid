package org.fs.dx.presenters;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.fs.common.AbstractPresenter;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.commons.SimpleTextWatcher;
import org.fs.dx.entities.Element;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.dx.views.INewElementView;
import org.fs.util.StringUtility;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.presenters.NewElementPresenter
 */
public class NewElementPresenter extends AbstractPresenter<INewElementView> implements INewElementPresenter, View.OnClickListener {

    public static final String KEY_ELEMENT = "bundle.element";

    final SimpleTextWatcher textWatcher = new SimpleTextWatcher() {

        @Override public void afterTextChanged(Editable editable) {
            if (view.isAvailable()) {
                String str = editable.toString();
                if (StringUtility.isNullOrEmpty(str)) {
                    String error = view.getContext().getString(R.string.new_element_empty_text_error);
                    view.showError(error);
                } else {
                    SpannableString spannedStr = ChemistryUtility.toChemistryText(str);
                    if (!spannedStr.toString().equalsIgnoreCase(editable.toString())) {
                        view.setText(spannedStr);
                    }
                    view.hideError();
                }
            }
        }
    };


    public NewElementPresenter(INewElementView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        if (restoreState != null) {
            Element element = restoreState.getParcelable(KEY_ELEMENT);
            if (element != null) {
                view.setElement(element);
            }
        }
    }

    @Override public void storeState(Bundle storeState) {
        Element element = view.getElement();
        if (element != null) {
            storeState.putParcelable(KEY_ELEMENT, element);
        }
    }

    @Override public View.OnClickListener provideViewClickListener() {
        return this;
    }

    @Override public TextWatcher provideTextWatcher() {
        return textWatcher;
    }

    @Override public void onCreate() {
        if (view.isAvailable()) {
            view.setupView();
        }
    }

    @Override public void onStart() {
        if(view.isAvailable()) {
            Element element = view.getElement();
            if (element != null) {
                SpannableString str = ChemistryUtility.toChemistryText(element.getName());
                view.setText(str);
            }
            //sets as full-screen-width
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
        //no-op
    }

    @Override public void onDestroy() {
        //no-op
    }

    @Override protected String getClassTag() {
        return NewElementPresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onClick(View v) {
        if(view.isAvailable()) {
            INewElementView.Callback callback = view.getCallback();
            Element element = view.getElement();
            String text = view.getText();
            switch (v.getId()) {
                case R.id.btnOk: {
                    if (StringUtility.isNullOrEmpty(text)) {
                        String error = view.getContext().getString(R.string.new_element_empty_text_error);
                        view.showError(error);
                    } else {
                        if (element == null) {
                            element = new Element();
                            element.setName(text);
                        }
                        if (callback != null) {
                            callback.onSaveOrUpdate(element);
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
}