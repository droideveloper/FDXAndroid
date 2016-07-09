package org.fs.dx.holders;

import android.support.v4.util.Pair;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.dx.R;
import org.fs.dx.commons.OnFormulaSelectedListener;
import org.fs.dx.entities.Molecule;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.util.ViewUtility;

import java.util.Locale;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.holders.FormulaRecyclerAdapter
 */
public class FormulaRecyclerViewHolder extends AbstractRecyclerViewHolder<Pair<Molecule, Double>> implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtPercentage;

    private OnFormulaSelectedListener selectedListener;

    private Pair<Molecule, Double> data;

    public FormulaRecyclerViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        txtTitle = ViewUtility.findViewById(view, R.id.txtMolecule);
        txtPercentage = ViewUtility.findViewById(view, R.id.txtMoleculeInput);
    }

    public void setSelectedListener(OnFormulaSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override protected String getClassTag() {
        return FormulaRecyclerViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override protected void onBindView(Pair<Molecule, Double> data) {
        String str = String.format(Locale.getDefault(), "%.2f", data.second);
        str += " %";
        txtPercentage.setText(str);
        SpannableString spannedStr = ChemistryUtility.toChemistryText(data.first.getName());
        txtTitle.setText(spannedStr);
    }

    public void setData(Pair<Molecule, Double> data) {
        this.data = data;
        onBindView(data);
    }

    @Override public void onClick(View view) {
        if (selectedListener != null) {
            selectedListener.onSelected(data);
        }
    }
}