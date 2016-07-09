package org.fs.dx.holders;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.dx.R;
import org.fs.dx.commons.OnMoleculeSelectedListener;
import org.fs.dx.entities.Molecule;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.util.ViewUtility;

import java.util.Locale;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.holders.MoleculeRecyclerViewHolder
 */
public class MoleculeRecyclerViewHolder extends AbstractRecyclerViewHolder<Molecule> implements View.OnClickListener {

    private Molecule molecule;
    private TextView txtMolecule;
    private OnMoleculeSelectedListener selectedListener;

    public MoleculeRecyclerViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        txtMolecule = ViewUtility.findViewById(view, R.id.txtMolecule);
    }

    public void setSelectedListener(OnMoleculeSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override protected void onBindView(Molecule data) {
        SpannableString str = ChemistryUtility.toChemistryText(data.getName());
        //put price
        String priceStr = String.format(Locale.getDefault(), "\n$ %.2f", data.getPrice());
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.append(priceStr);

        txtMolecule.setText(builder);
    }

    public void setData(Molecule molecule) {
        this.molecule = molecule;
        onBindView(molecule);
    }

    @Override protected String getClassTag() {
        return MoleculeRecyclerViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public void onClick(View view) {
        if (selectedListener != null) {
            selectedListener.onSelected(molecule);
        }
    }
}