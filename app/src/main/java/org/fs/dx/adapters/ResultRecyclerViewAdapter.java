package org.fs.dx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.dx.R;
import org.fs.dx.entities.Result;
import org.fs.dx.holders.ResultRecyclerViewHolder;
import org.fs.util.Collections;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.adapters.ResultRecyclerViewAdapter
 */
public class ResultRecyclerViewAdapter extends AbstractRecyclerAdapter<Result, ResultRecyclerViewHolder> {

    public ResultRecyclerViewAdapter(Context context) {
        super(new ArrayList<Result>(), context);
    }

    @Override public ResultRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater factory = inflaterFactory();
        if (factory != null) {
            View view = factory.inflate(R.layout.result_view, parent, false);
            return new ResultRecyclerViewHolder(view);
        }
        return null;
    }

    @Override public void onBindViewHolder(ResultRecyclerViewHolder holder, int position) {
        final Result result = getItemAt(position);
        if (holder != null) {
            holder.setResult(result);
        }
    }

    public Result removeAt(int index) {
        if (index >= 0 && index < getItemCount()) {
            Result result = getItemAt(index);
            removeData(result);
            return result;
        }
        return null;
    }

    public void update(Map<String, Double> totals) {
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            if (!dataSet.isEmpty()) {
                for (int i = 0; i < getItemCount(); i++) {
                    Result result = getItemAt(i);
                    if (containsName(entry.getKey())) {
                        if (result.getName().equalsIgnoreCase(entry.getKey())) {
                            if (result.getSum() != entry.getValue()) {
                                result.setSum(entry.getValue());
                                //item changed
                                notifyItemChanged(positionOf(result));
                            }
                        }
                    } else {
                        Result rs = new Result();
                        rs.setName(entry.getKey());
                        rs.setSum(entry.getValue());
                        //inserted
                        appendData(rs, false);
                    }
                }
            } else {
                Result rs = new Result();
                rs.setName(entry.getKey());
                rs.setSum(entry.getValue());
                //inserted
                appendData(rs, false);
            }
        }
        //then delete those no longer needed
        for (int i = 0; i < getItemCount(); i++) {
            Result result = getItemAt(i);
            if (!totals.containsKey(result.getName())) {
                removeData(result);
            }
        }
    }

    public void clear() {
        if (!Collections.isNullOrEmpty(dataSet)) {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }

    public boolean containsName(String name) {
        for (int i = 0, z = getItemCount(); i < z; i++) {
            Result result = getItemAt(i);
            if (result.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public int positionOf(Result result) {
        if (result != null && dataSet != null) {
            return dataSet.indexOf(result);
        }
        return -1;
    }

    @Override public int getItemViewType(int position) {
        return 0;
    }

    @Override protected String getClassTag() {
        return ResultRecyclerViewAdapter.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }
}