package org.fs.dx.holders;

import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.dx.R;
import org.fs.dx.commons.OnElementSelectedListener;
import org.fs.dx.entities.Element;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.util.ViewUtility;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.holders.ElementRecyclerViewHolder
 */
public class ElementRecyclerViewHolder extends AbstractRecyclerViewHolder<Element> implements View.OnClickListener {

    private TextView txtElement;
    private OnElementSelectedListener selectedListener;
    private Element element;

    public ElementRecyclerViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        txtElement = ViewUtility.findViewById(view, R.id.txtElement);
    }

    @Override protected String getClassTag() {
        return ElementRecyclerViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override protected void onBindView(Element element) {
        SpannableString str = ChemistryUtility.toChemistryText(element.getName());
        txtElement.setText(str);
    }

    public void setData(Element element) {
        this.element = element;
        onBindView(element);
    }

    public void setSelectedListener(OnElementSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override public void onClick(View view) {
        if (selectedListener != null) {
            selectedListener.onSelected(element);
        }
    }
}