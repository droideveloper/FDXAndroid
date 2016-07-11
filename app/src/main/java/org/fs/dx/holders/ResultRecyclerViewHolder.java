package org.fs.dx.holders;

import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.dx.R;
import org.fs.dx.entities.Result;
import org.fs.dx.utilities.ChemistryUtility;
import org.fs.util.ViewUtility;

import java.util.Locale;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.holders.ResultRecyclerViewHolder
 */
public class ResultRecyclerViewHolder extends AbstractRecyclerViewHolder<Result> {

    private final static String TOTAL_STR  = "TOTAL";

    private TextView txtNameView;
    private TextView txtTotalView;

    public ResultRecyclerViewHolder(View view) {
        super(view);
        txtNameView = ViewUtility.findViewById(view, R.id.txtNameView);
        txtTotalView = ViewUtility.findViewById(view, R.id.txtTotalView);
    }

    @Override protected String getClassTag() {
        return ResultRecyclerViewHolder.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override protected void onBindView(Result result) {
        SpannableString str = ChemistryUtility.toChemistryText(result.getName());
        txtNameView.setText(str);
        if (str.toString().equalsIgnoreCase(TOTAL_STR)) {
            String sum = String.format(Locale.getDefault(), "%.2f", result.getSum());
            String sumStr = "$ " + sum;
            txtTotalView.setText(sumStr);
        } else {
            String sum = String.format(Locale.getDefault(), "%.4f", result.getSum());
            sum += " %";
            txtTotalView.setText(sum);
        }
    }

    public void setResult(Result result) {
        onBindView(result);
    }
}