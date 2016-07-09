package org.fs.dx.presenters;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.fs.common.AbstractPresenter;
import org.fs.core.AbstractApplication;
import org.fs.dx.R;
import org.fs.dx.adapters.MoleculeAdapter;
import org.fs.dx.entities.Molecule;
import org.fs.dx.usecases.QueryMoleculesUseCase;
import org.fs.dx.usecases.QueryMoleculesUseCaseImp;
import org.fs.dx.views.ISelectMoleculeView;
import org.fs.util.Collections;
import org.fs.util.StringUtility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.SelectMoleculesPresenter
 */
public class SelectMoleculePresenter extends AbstractPresenter<ISelectMoleculeView> implements ISelectMoleculePresenter,
                                                                                                Spinner.OnItemSelectedListener,
                                                                                                View.OnClickListener,
                                                                                                TextWatcher,
                                                                                                QueryMoleculesUseCase.Callback {
    public final static String KEY_STR = "bundle.str";
    public final static String KEY_MOL = "bundle.mol";

    private String   str;
    private Molecule selected;

    private MoleculeAdapter adapter;

    public SelectMoleculePresenter(ISelectMoleculeView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        if (restoreState != null) {
            str = restoreState.getString(KEY_STR);
            selected = restoreState.getParcelable(KEY_MOL);
        }
    }

    @Override public void storeState(Bundle storeState) {
        if (str != null) {
            storeState.putString(KEY_STR, str);
        }
        if (selected != null) {
            storeState.putParcelable(KEY_MOL, selected);
        }
    }

    @Override public Spinner.OnItemSelectedListener provideSpinnerSelectionListener() {
        return this;
    }

    @Override public TextWatcher provideTextWatcher() {
        return this;
    }

    @Override public View.OnClickListener provideViewClickListener() {
        return this;
    }

    @Override public void onCreate() {
        if (view.isAvailable()) {
            view.setupView();
        }
    }

    @Override public void onStart() {
        if (view.isAvailable()) {
            final Dialog dialog = view.getDialog();
            if (dialog != null) {
                final Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                     ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
            if (adapter == null) {
                adapter = new MoleculeAdapter(view.getContext());
                view.bindAdapter(adapter);

                QueryMoleculesUseCase usecase = new QueryMoleculesUseCaseImp();
                usecase.setDatabaseManager(view.getDatabaseManager());
                usecase.executeAsync(this);
            }
            if (str != null) {
                view.setStr(str);
            }
            if (selected != null) {
                int position = adapter.positionOf(selected);
                view.setSelection(position);
            }
        }
    }

    @Override public void onStop() {
        //no op
    }

    @Override public void onDestroy() {
        //no op
    }

    @Override public void onSuccess(List<Molecule> dataSet) {
        if (view.isAvailable()) {
            if (!Collections.isNullOrEmpty(dataSet)) {
                if (adapter != null) {
                    adapter.addAll(dataSet);
                }
            }
        }
    }

    @Override public void onError(Throwable tr) {
        if (view.isAvailable()) {
            StringWriter str = new StringWriter(128);
            PrintWriter ptr = new PrintWriter(str);
            tr.printStackTrace(ptr);
            view.showError(str.toString());
        }
    }

    @Override public void onClick(View v) {
        if (view.isAvailable()) {
            final int id = v.getId();
            final ISelectMoleculeView.Callback callback = view.getCallback();
            switch (id) {
                case R.id.btnOk: {
                    if (!StringUtility.isNullOrEmpty(str) && selected != null) {
                        if (callback != null) {
                            callback.onSuccess(selected, Double.parseDouble(str));
                        }
                        view.dismiss();
                    } else {
                        String error = view.getContext().getString(R.string.nbr_text_error);
                        view.showError(error);
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

    @Override public void onItemSelected(AdapterView<?> adapterView, View v, int index, long l) {
        if (view.isAvailable()) {
            selected = adapter.getItemAt(index);
        }
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) {
        //no op
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
                view.hideError();
            }
        }
    }

    @Override protected String getClassTag() {
        return SelectMoleculePresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}