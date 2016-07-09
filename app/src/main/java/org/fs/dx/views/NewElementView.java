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
import org.fs.dx.entities.Element;
import org.fs.dx.presenters.INewElementPresenter;
import org.fs.dx.presenters.NewElementPresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.views.NewElementView
 */
public class NewElementView extends AbstractDialogFragment<INewElementPresenter> implements INewElementView {

    private Callback callback;
    private Element  element;

    private Button          btnOk;
    private Button          btnCancel;
    private EditText        txtInput;
    private TextInputLayout txtInputLayout;

    public NewElementView() {
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

    @Override public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public View bindView(LayoutInflater factory, ViewGroup parent) {
        View view = factory.inflate(R.layout.layout_new_element_view, parent, false);
        btnOk = ViewUtility.findViewById(view, R.id.btnOk);
        btnCancel = ViewUtility.findViewById(view, R.id.btnCancel);
        txtInput = ViewUtility.findViewById(view, R.id.txtElementInput);
        txtInputLayout = ViewUtility.findViewById(view, R.id.txtInputLayout);
        return view;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.storeState(outState);
    }

    @Override public void setupView() {
        btnCancel.setOnClickListener(presenter.provideViewClickListener());
        btnOk.setOnClickListener(presenter.provideViewClickListener());
        //add textWatcher
        txtInput.addTextChangedListener(presenter.provideTextWatcher());
    }

    @Override public void showError(String str) {
        txtInputLayout.setErrorEnabled(true);
        txtInputLayout.setError(str);
    }

    @Override public void hideError() {
        txtInputLayout.setErrorEnabled(false);
        txtInputLayout.setError(null);
    }

    @Override public Callback getCallback() {
        return callback;
    }

    @Override public void setText(SpannableString str) {
        txtInput.setText(str);
    }

    @Override public Element getElement() {
        return element;
    }

    @Override public String getText() {
        return txtInput.getText().toString();
    }

    @Override public void setElement(Element element) {
        this.element = element;
    }

    @Override public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override public boolean isAvailable() {
        return isCallingSafe();
    }

    @Override protected INewElementPresenter presenter() {
        return new NewElementPresenter(this);
    }

    @Override protected String getClassTag() {
        return NewElementView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}