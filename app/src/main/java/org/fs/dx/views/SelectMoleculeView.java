package org.fs.dx.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractDialogFragment;
import org.fs.dx.R;
import org.fs.dx.adapters.MoleculeAdapter;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.dx.presenters.ISelectMoleculePresenter;
import org.fs.dx.presenters.SelectMoleculePresenter;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.SelectMoleculeView
 */
public class SelectMoleculeView extends AbstractDialogFragment<ISelectMoleculePresenter> implements ISelectMoleculeView {

    private TextInputLayout txtInputLayout;
    private EditText        txtTextInput;
    private Spinner         listMolecules;
    private Button          btnOk;
    private Button          btnCancel;

    private Callback                    callback;
    private IDatabaseManager            dbManager;

    public SelectMoleculeView() {
        super();
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return bindView(inflater, container);
    }

    @Override public View bindView(LayoutInflater factory, ViewGroup parent) {
        View view = factory.inflate(R.layout.layout_molecule_assign, parent, false);
        listMolecules = ViewUtility.findViewById(view, R.id.listMolecules);
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

    @Override public void bindAdapter(MoleculeAdapter adapter) {
        listMolecules.setAdapter(adapter);
    }

    @Override public void setupView() {
        btnOk.setOnClickListener(presenter.provideViewClickListener());
        btnCancel.setOnClickListener(presenter.provideViewClickListener());
        //textWatcher
        txtTextInput.addTextChangedListener(presenter.provideTextWatcher());
        //selection
        listMolecules.setOnItemSelectedListener(presenter.provideSpinnerSelectionListener());
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

    @Override public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override public void showError(String text) {
        txtInputLayout.setErrorEnabled(true);
        txtInputLayout.setError(text);
    }

    @Override public void hideError() {
        txtInputLayout.setErrorEnabled(false);
        txtInputLayout.setError(null);
    }

    @Override public void setSelection(int selection) {
        listMolecules.setSelection(selection);
    }

    @Override public void setStr(String str) {
        txtTextInput.setText(str);
    }

    @Override public void setDatabaseManager(IDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override public IDatabaseManager getDatabaseManager() {
        return dbManager;
    }

    @Override public boolean isAvailable() {
        return isCallingSafe();
    }

    @Override public boolean isErrorShown() {
        return txtInputLayout.isErrorEnabled();
    }

    @Override public Callback getCallback() {
        return callback;
    }

    @Override protected ISelectMoleculePresenter presenter() {
        return new SelectMoleculePresenter(this);
    }

    @Override protected String getClassTag() {
        return SelectMoleculeView.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}