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
import java.util.Comparator;
import java.util.Iterator;
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
            final String keyName = entry.getKey();
            //compare previous data if exits update it
            if (!dataSet.isEmpty()) {
                for (int i = 0; i < getItemCount(); i++) {
                    final Result result = getItemAt(i);
                    final String resultName = result.getName();
                    if (containsName(keyName)) {
                        if (resultName.equalsIgnoreCase(keyName)) {
                            if (result.getSum() != entry.getValue()) {
                                result.setSum(entry.getValue());
                                //item changed
                                final int index = dataSet.indexOf(result);
                                notifyItemChanged(index);
                            }
                        }
                    //if we can not find it in there we need to handle this way
                    } else {
                        Result rs = new Result();
                        rs.setName(keyName);
                        rs.setSum(entry.getValue());
                        //inserted
                        appendData(rs, false);
                    }
                }
            //if we are here first of all we just update this way
            } else {
                //if there is nothing in our dataSet we just put everything in
                Result rs = new Result();
                rs.setName(keyName);
                rs.setSum(entry.getValue());
                //inserted
                appendData(rs, false);
            }
        }
        //if we send nothing it should clean everything
        if (totals.isEmpty()) {
            clear();//just clean it up
        } else {
            //previous leftovers needs clean up
            filter(totals, new IPredicate() {
                @Override public boolean apply(Map<String, Double> filter, Result result) {
                    for (Map.Entry<String, Double> entry : filter.entrySet()) {
                        final String entryName = entry.getKey();
                        final String resultName = result.getName();
                        if (entryName.equalsIgnoreCase(resultName)) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        }
        //this will sort it if dataSet is not empty
        if (!dataSet.isEmpty()) {
            java.util.Collections.sort(dataSet, new Comparator<Result>() {
                @Override public int compare(Result prev, Result next) {
                    final String prevName = prev.getName();
                    final String nextName = next.getName();
                    final int prevIndex = dataSet.indexOf(prev);
                    final int nextIndex = dataSet.indexOf(next);
                    final int compare = prevName.compareTo(nextName);
                    //say adapter that it's changing
                    if (compare < 0) {
                        notifyItemMoved(prevIndex, nextIndex);
                    } else if (compare > 0) {
                        notifyItemChanged(nextIndex, prevIndex);
                    }
                    return compare;
                }
            });
        }
    }

    //will do filtering for some of data needs to be recycled
    private void filter(Map<String, Double> filterable, IPredicate filter) {
        Iterator<Result> resultIterator = dataSet.iterator();
        while (resultIterator.hasNext()) {
            final Result result = resultIterator.next();
            if (filter.apply(filterable, result)) {
                final int index = dataSet.indexOf(result);
                resultIterator.remove();
                notifyItemRemoved(index);
            }
        }
    }

    public void clear() {
        if (!Collections.isNullOrEmpty(dataSet)) {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }

    //find if that name is present
    public boolean containsName(String name) {
        //in this segment do not cache size value since we start to delete some of it
        for (int i = 0; i < getItemCount(); i++) {
            Result result = getItemAt(i);
            final String resultName = result.getName();
            if (resultName.equalsIgnoreCase(name)) {
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

    /**
     * Used for filtering non existing values inside of our result set
     * that purpose we need to check each individual item in our new data update set
     */
    private interface IPredicate {
        boolean apply(Map<String, Double> filter, Result result);
    }
}