package org.fs.dx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractListAdapter;
import org.fs.dx.R;
import org.fs.dx.entities.Element;
import org.fs.dx.holders.ElementViewHolder;

import java.util.List;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.adapters.ElementAdapter
 */
public class ElementAdapter extends AbstractListAdapter<Element, ElementViewHolder> {

    public ElementAdapter(Context context) {
        super(context);
    }

    public ElementAdapter(Context context, List<Element> dataSet) {
        super(context, dataSet);
    }

    @Override protected ElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.layout_spinner_list, parent, false);
            return new ElementViewHolder(view);
        }
        return null;
    }

    @Override protected void onBindViewHolder(ElementViewHolder viewHolder, int position) {
        final Element element = getItemAt(position);
        if (viewHolder != null) {
            viewHolder.setData(element);
        }
    }

    public int positionOf(Element selected) {
        return dataSet.indexOf(selected);
    }

    @Override protected String getClassTag() {
        return ElementAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}