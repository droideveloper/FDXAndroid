package org.fs.dx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractListAdapter;
import org.fs.dx.R;
import org.fs.dx.entities.Molecule;
import org.fs.dx.holders.MoleculeViewHolder;

import java.util.ArrayList;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.adapters.MoleculeAdapter
 */
public class MoleculeAdapter extends AbstractListAdapter<Molecule, MoleculeViewHolder> {

    public MoleculeAdapter(Context context) {
        super(context, new ArrayList<Molecule>());
    }

    @Override protected MoleculeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.layout_spinner_list, parent, false);
            return new MoleculeViewHolder(view);
        }
        return null;
    }

    @Override protected void onBindViewHolder(MoleculeViewHolder viewHolder, int position) {
        final Molecule molecule = getItemAt(position);
        if (viewHolder != null) {
            viewHolder.setData(molecule);
        }
    }

    public int positionOf(Molecule molecule) {
        return dataSet.indexOf(molecule);
    }

    @Override protected String getClassTag() {
        return MoleculeAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}