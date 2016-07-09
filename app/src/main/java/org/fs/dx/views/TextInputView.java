package org.fs.dx.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractDialogFragment;
import org.fs.dx.R;
import org.fs.dx.presenters.ITextInputPresenter;
import org.fs.dx.presenters.TextInputPresenter;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.TextInputView
 */
public class TextInputView extends AbstractDialogFragment<ITextInputPresenter> implements ITextInputView {

    private TextInputLayout txtInputLayout;
    private EditText        txtTextInput;
    private Button          btnOk;
    private Button          btnCancel;

    private Callback        callback;

    public TextInputView() {
        super();
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(inflater, container);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.restoreState(savedInstanceState != null ? savedInstanceState : getArguments());
        presenter.onCreate();
    }

    @Override public View bindView(LayoutInflater factory, ViewGroup parent) {
        View view = factory.inflate(R.layout.layout_text_input, parent, false);
        txtInputLayout = ViewUtility.findViewById(view, R.id.txtInputLayout);
        txtTextInput   = ViewUtility.findViewById(view, R.id.txtTextInput);
        btnOk = ViewUtility.findViewById(view, R.id.btnOk);
        btnCancel = ViewUtility.findViewById(view, R.id.btnCancel);
        return view;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.storeState(outState);
    }

    @Override public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override public void setStr(String str) {
        SpannableString spannedStr = ChemistryUtility.toChemistryText(str);
        txtTextInput.setText(spannedStr);
    }

    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void setupView() {
        //click listener
        btnOk.setOnClickListener(presenter.provideClickListener());
        btnCancel.setOnClickListener(presenter.provideClickListener());
        //add textWatcher
        txtTextInput.addTextChangedListener(presenter.provideTextWatcher());
    }

    @Override public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override public Callback getCallback() {
        return callback;
    }

    @Override public void showError(String error) {
        txtInputLayout.setErrorEnabled(true);
        txtInputLayout.setError(error);
    }

    @Override public boolean isAvailable() {
        return isCallingSafe();
    }

    @Override public boolean isErrorShown() {
        return txtInputLayout.isErrorEnabled();
    }

    @Override public void hideError() {
        txtInputLayout.setErrorEnabled(false);
        txtInputLayout.setError(null);
    }

    @Override protected ITextInputPresenter presenter() {
        return new TextInputPresenter(this);
    }

    @Override protected String getClassTag() {
        return TextInputView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}