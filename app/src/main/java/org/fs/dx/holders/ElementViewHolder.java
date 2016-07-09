package org.fs.dx.holders;

import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractViewHolder;
import org.fs.dx.entities.Element;
import org.fs.dx.utilities.ChemistryUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.adapters.ElementViewHolder
 */
public class ElementViewHolder extends AbstractViewHolder<Element> {

    private TextView txtView;

    public ElementViewHolder(View view) {
        super(view);
        txtView = (TextView) view;
    }

    public void setData(Element data) {
        this.data = data;
        onBindView(data);
    }

    public Element getData() {
        return this.data;
    }

    @Override protected void onBindView(Element element) {
        SpannableString str = ChemistryUtility.toChemistryText(element.getName());
        txtView.setText(str);
    }

    @Override protected String getClassTag() {
        return ElementViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}