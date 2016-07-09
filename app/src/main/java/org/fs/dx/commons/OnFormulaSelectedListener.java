package org.fs.dx.commons;

import android.support.v4.util.Pair;

import org.fs.dx.entities.Molecule;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.commons.OnFormulaSelectedListener
 */
public interface OnFormulaSelectedListener {
    void onSelected(Pair<Molecule, Double> selected);
}
