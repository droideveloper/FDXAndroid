package org.fs.dx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.dx.R;
import org.fs.dx.commons.OnElementSelectedListener;
import org.fs.dx.entities.Element;
import org.fs.dx.holders.ElementRecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.adapters.ElementRecyclerViewAdapter
 */
public class ElementRecyclerViewAdapter extends AbstractRecyclerAdapter<Element, ElementRecyclerViewHolder> {

    private OnElementSelectedListener selectedListener;

    public ElementRecyclerViewAdapter(Context context) {
        super(new ArrayList<Element>(), context);
    }

    public void setSelectedListener(OnElementSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override public void onViewAttachedToWindow(ElementRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setSelectedListener(selectedListener);
    }

    @Override public void onViewDetachedFromWindow(ElementRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setSelectedListener(null);
    }

    @Override public ElementRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.element_name_view, parent, false);
            return new ElementRecyclerViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(ElementRecyclerViewHolder holder, int position) {
        final Element element = getItemAt(position);
        if (holder != null) {
            holder.setData(element);
        }
    }

    public Element removeAt(int index) {
        if (index >= 0 && index < getItemCount()) {
            Element element = getItemAt(index);
            removeData(element);
            return element;
        }
        return null;
    }

    public int positionOf(Element element) {
        if (element != null && dataSet != null) {
            return dataSet.indexOf(element);
        }
        return -1;
    }

    public void appendAt(Element element, int index) {
        if (index >= 0) {
            dataSet.add(index, element);
            notifyItemInserted(index);
        }
    }

    @Override public int getItemViewType(int position) {
        return 0;
    }

    @Override
    protected String getClassTag() {
        return ElementRecyclerViewAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}