package org.fs.dx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.dx.R;
import org.fs.dx.commons.OnReferenceSelectedListener;
import org.fs.dx.entities.Reference;
import org.fs.dx.holders.ReferenceRecyclerViewHolder;
import org.fs.dx.managers.IDatabaseManager;

import java.util.ArrayList;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.adapters.ReferenceRecyclerViewAdapter
 */
public class ReferenceRecyclerViewAdapter extends AbstractRecyclerAdapter<Reference, ReferenceRecyclerViewHolder> {

    private IDatabaseManager                dbManager;
    private OnReferenceSelectedListener     selectedListener;

    public ReferenceRecyclerViewAdapter(Context context) {
        super(new ArrayList<Reference>(), context);
    }

    public void setDbManager(IDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setSelectedListener(OnReferenceSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override public void onViewAttachedToWindow(ReferenceRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setDbManager(dbManager);
        holder.setSelectedListener(selectedListener);
    }

    @Override public void onViewDetachedFromWindow(ReferenceRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setDbManager(null);
        holder.setSelectedListener(null);
    }

    @Override public ReferenceRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.element_view, parent, false);
            return new ReferenceRecyclerViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(ReferenceRecyclerViewHolder holder, int position) {
        final Reference reference = getItemAt(position);
        if (holder != null) {
            holder.setDbManager(dbManager);
            holder.setReference(reference);
        }
    }

    public Reference removeAt(int index) {
        if (index >= 0 && index < getItemCount()) {
            Reference reference = getItemAt(index);
            removeData(reference);
            return reference;
        }
        return null;
    }

    public int positionOf(Reference reference) {
        if (reference != null && dataSet != null) {
            return dataSet.indexOf(reference);
        }
        return -1;
    }

    public void appendAt(Reference reference, int index) {
        if (index >= 0) {
            dataSet.add(index, reference);
            notifyItemInserted(index);
        }
    }

    @Override public int getItemViewType(int position) {
        return 0;
    }

    @Override protected String getClassTag() {
        return ReferenceRecyclerViewAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}