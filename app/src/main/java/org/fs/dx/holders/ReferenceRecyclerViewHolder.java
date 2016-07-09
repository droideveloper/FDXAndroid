package org.fs.dx.holders;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.dx.R;
import org.fs.dx.commons.OnReferenceSelectedListener;
import org.fs.dx.entities.Element;
import org.fs.dx.entities.Reference;
import org.fs.dx.managers.IDatabaseManager;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.util.ViewUtility;

import java.util.Locale;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.holders.ReferenceRecyclerViewHolder
 */
public class ReferenceRecyclerViewHolder extends AbstractRecyclerViewHolder<Reference> implements View.OnClickListener {

    private Reference        reference;
    private IDatabaseManager dbManager;

    private OnReferenceSelectedListener selectedListener;

    private TextView    txtElement;
    private TextView    txtElementInput;

    public ReferenceRecyclerViewHolder(@NonNull View view) {
        super(view);
        view.setOnClickListener(this);
        txtElement = ViewUtility.findViewById(view, R.id.txtElement);
        txtElementInput = ViewUtility.findViewById(view, R.id.txtElementInput);
    }

    public void setDbManager(IDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override protected String getClassTag() {
        return ReferenceRecyclerViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override protected void onBindView(Reference reference) {
        if (reference != null) {
            String txtPercentage = String.format(Locale.getDefault(), "%.2f", reference.getPercentage());
            txtPercentage += " %";
            txtElementInput.setText(txtPercentage);

            Element element = dbManager.getElement(reference.getElementId());
            if (element != null) {
                SpannableString txtName = ChemistryUtility.toChemistryText(element.getName());
                txtElement.setText(txtName);
            }
        }
    }

    public void setReference(Reference reference) {
        this.reference = reference;
        onBindView(reference);
    }

    public void setSelectedListener(OnReferenceSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override public void onClick(View view) {
        if (selectedListener != null) {
            selectedListener.onSelected(reference);
        }
    }
}
