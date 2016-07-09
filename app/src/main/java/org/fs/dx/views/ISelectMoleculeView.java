package org.fs.dx.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.common.IView;
import org.fs.dx.adapters.MoleculeAdapter;
import org.fs.dx.entities.Molecule;
import org.fs.dx.managers.IDatabaseManager;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.views.ISelectMoleculeView
 */
public interface ISelectMoleculeView extends IView {

    View bindView(LayoutInflater factory, ViewGroup parent);
    void bindAdapter(MoleculeAdapter adapter);
    void setupView();

    void setCallback(Callback callback);

    void showError(String text);
    void hideError();

    void setSelection(int selection);
    void setStr(String str);
    void setDatabaseManager(IDatabaseManager dbManager);

    void dismiss();

    IDatabaseManager getDatabaseManager();
    boolean          isAvailable();
    boolean          isErrorShown();
    Context          getContext();
    Dialog           getDialog();
    Callback         getCallback();

    interface Callback {
        void onSuccess(Molecule molecule, double amount);
        void onCancel();
    }
}