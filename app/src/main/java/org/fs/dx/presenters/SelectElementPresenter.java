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
import org.fs.dx.adapters.ElementAdapter;
import org.fs.dx.entities.Element;
import org.fs.dx.entities.Reference;
import org.fs.dx.usecases.QueryElementsUseCase;
import org.fs.dx.usecases.QueryElementsUseCaseImp;
import org.fs.dx.views.ISelectElementView;
import org.fs.util.Collections;
import org.fs.util.StringUtility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.presenters.SelectElementPresenter
 */
public class SelectElementPresenter extends AbstractPresenter<ISelectElementView> implements ISelectElementPresenter,
                                                                                             Spinner.OnItemSelectedListener,
                                                                                             View.OnClickListener,
                                                                                             TextWatcher,
                                                                                             QueryElementsUseCase.Callback {

    public final static String KEY_STR = "bundle.str";
    public final static String KEY_ELM = "bundle.elm";
    public final static String KEY_REF = "bundle.ref";

    private String  str;
    private Element selected;

    private Reference       reference;
    private ElementAdapter  elementAdapter;

    public SelectElementPresenter(ISelectElementView view) {
        super(view);
    }

    @Override public void restoreState(Bundle restoreState) {
        if (restoreState != null) {
            str = restoreState.getString(KEY_STR);
            selected = restoreState.getParcelable(KEY_ELM);
            reference = restoreState.getParcelable(KEY_REF);
        }
    }

    @Override public void storeState(Bundle storeState) {
        if (str != null) {
            storeState.putString(KEY_STR, str);
        }
        if (selected != null) {
            storeState.putParcelable(KEY_ELM, selected);
        }
        if (reference != null) {
            storeState.putParcelable(KEY_REF, reference);
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
            if (elementAdapter == null) {
                elementAdapter = new ElementAdapter(view.getContext());
                view.bindAdapter(elementAdapter);

                QueryElementsUseCase usecase = new QueryElementsUseCaseImp();
                usecase.setDatabaseManager(view.getDatabaseManager());
                usecase.executeAsync(this);
            }
            if (selected != null) {
                int position = elementAdapter.positionOf(selected);
                view.setSelection(position);
            }
            if (str != null) {
                view.setStr(str);
            }
        }
    }

    @Override public void onStop() {
        //no op
    }

    @Override public void onDestroy() {
        //no op
    }

    @Override public void onClick(View v) {
        if (view.isAvailable()) {
            final int id = v.getId();
            final ISelectElementView.Callback callback = view.getCallback();
            switch (id) {
                case R.id.btnOk: {
                    if (!StringUtility.isNullOrEmpty(str) && selected != null) {
                        if (reference == null) {
                            reference = new Reference();
                        }
                        reference.setElementId(selected.getId());
                        reference.setPercentage(Double.parseDouble(str));
                        if (callback != null) {
                            callback.onSuccess(reference);
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
            selected = elementAdapter.getItemAt(index);
        }
    }

    @Override public void onNothingSelected(AdapterView<?> adapter) {
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
        return SelectElementPresenter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onSuccess(List<Element> dataSet) {
        if (view.isAvailable()) {
            if (!Collections.isNullOrEmpty(dataSet)) {
                if (elementAdapter != null) {
                    elementAdapter.addAll(dataSet);
                }
            }
        }
    }

    @Override public void onError(Throwable throwable) {
        if (view.isAvailable()) {
            StringWriter str = new StringWriter(128);
            PrintWriter  ptr = new PrintWriter(str);
            throwable.printStackTrace(ptr);
            view.showError(str.toString());
        }
    }
}