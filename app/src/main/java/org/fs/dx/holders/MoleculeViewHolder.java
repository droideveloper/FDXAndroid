package org.fs.dx.holders;

import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractViewHolder;
import org.fs.dx.entities.Molecule;
import org.fs.dx.utilities.ChemistryUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.holders.MoleculeViewHolder
 */
public class MoleculeViewHolder extends AbstractViewHolder<Molecule> {

    private TextView txtView;

    public MoleculeViewHolder(View view) {
        super(view);
        txtView = (TextView) view;
    }

    public void setData(Molecule data) {
        this.data = data;
        onBindView(data);
    }

    public Molecule getData() {
        return this.data;
    }

    @Override protected void onBindView(Molecule data) {
        SpannableString str = ChemistryUtility.toChemistryText(data.getName());
        txtView.setText(str);
    }

    @Override protected String getClassTag() {
        return MoleculeViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}