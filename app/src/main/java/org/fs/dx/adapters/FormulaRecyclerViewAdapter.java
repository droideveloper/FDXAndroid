package org.fs.dx.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.dx.R;
import org.fs.dx.commons.OnFormulaSelectedListener;
import org.fs.dx.entities.Molecule;
import org.fs.dx.holders.FormulaRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.adapters.FormulaRecyclerViewAdapter
 */
public class FormulaRecyclerViewAdapter extends AbstractRecyclerAdapter<Pair<Molecule, Double>, FormulaRecyclerViewHolder> {

    private OnFormulaSelectedListener selectedListener;

    public FormulaRecyclerViewAdapter(Context context) {
        super(new ArrayList<Pair<Molecule, Double>>(), context);
    }

    public void setSelectedListener(OnFormulaSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override public void onViewAttachedToWindow(FormulaRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setSelectedListener(selectedListener);
    }

    @Override public void onViewDetachedFromWindow(FormulaRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setSelectedListener(null);
    }

    @Override public FormulaRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.molecule_view, parent, false);
            return new FormulaRecyclerViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(FormulaRecyclerViewHolder holder, int position) {
        final Pair<Molecule, Double> data = getItemAt(position);
        if (holder != null) {
            holder.setData(data);
        }
    }

    public Pair<Molecule, Double> removeAt(int index) {
        if (index >= 0 && index < getItemCount()) {
            Pair<Molecule, Double> pair = getItemAt(index);
            removeData(pair);
            return pair;
        }
        return null;
    }

    public void setAt(Pair<Molecule, Double> pair, int index) {
        if (index >= 0 && index < getItemCount()) {
            dataSet.set(index, pair);
            notifyItemChanged(index);
        }
    }

    public int positionOf(Pair<Molecule, Double> pair) {
        if (pair != null && dataSet != null) {
            return dataSet.indexOf(pair);
        }
        return -1;
    }

    public void clear() {
        if (!org.fs.util.Collections.isNullOrEmpty(dataSet)) {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }

    public List<Pair<Molecule, Double>> provideClone() {
        ArrayList<Pair<Molecule, Double>> clone = new ArrayList<>(dataSet);
        Collections.copy(clone, dataSet);
        return clone;
    }

    public void appendAt(Pair<Molecule, Double> pair, int index) {
        if (index >= 0) {
            dataSet.add(index, pair);
            notifyItemInserted(index);
        }
    }

    @Override public int getItemViewType(int position) {
        return 0;
    }

    @Override protected String getClassTag() {
        return FormulaRecyclerViewAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}