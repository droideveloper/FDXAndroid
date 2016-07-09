package org.fs.dx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.dx.R;
import org.fs.dx.commons.OnMoleculeSelectedListener;
import org.fs.dx.entities.Molecule;
import org.fs.dx.holders.MoleculeRecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by Fatih on 07/07/16.
 * as org.fs.dx.adapters.MoleculeRecyclerViewAdapter
 */
public class MoleculeRecyclerViewAdapter extends AbstractRecyclerAdapter<Molecule, MoleculeRecyclerViewHolder> {

    private OnMoleculeSelectedListener selectedListener;

    public MoleculeRecyclerViewAdapter(Context context) {
        super(new ArrayList<Molecule>(), context);
    }

    public void setSelectedListener(OnMoleculeSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override public MoleculeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.molecule_name_view, parent, false);
            return new MoleculeRecyclerViewHolder(view);
        }
        return null;
    }

    @Override public void onViewAttachedToWindow(MoleculeRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setSelectedListener(selectedListener);
    }

    @Override public void onViewDetachedFromWindow(MoleculeRecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setSelectedListener(null);
    }

    @Override public void onBindViewHolder(MoleculeRecyclerViewHolder holder, int position) {
        final Molecule molecule = getItemAt(position);
        if (holder != null) {
            holder.setData(molecule);
        }
    }

    public Molecule removeAt(int index) {
        if (index >= 0 && index < getItemCount()) {
            Molecule molecule = getItemAt(index);
            removeData(molecule);
            return molecule;
        }
        return null;
    }

    public int positionOf(Molecule molecule) {
        if (molecule != null && dataSet != null) {
            return dataSet.indexOf(molecule);
        }
        return -1;
    }

    public void appendAt(Molecule molecule, int index) {
        if (index >= 0) {
            dataSet.add(index, molecule);
            notifyItemInserted(index);
        }
    }

    @Override public int getItemViewType(int position) {
        return 0;
    }

    @Override protected String getClassTag() {
        return MoleculeRecyclerViewAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}