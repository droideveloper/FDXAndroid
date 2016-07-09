package org.fs.dx.entities;

import android.os.Parcel;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractEntity;
import org.fs.util.StringUtility;

/**
 * Created by Fatih on 08/07/16.
 * as org.fs.dx.entities.Result
 */
public final class Result extends AbstractEntity {

    private String name;
    private double sum;

    public Result() {}
    public Result(Parcel input) {
        super(input);
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override protected void readParcel(Parcel input) {
        boolean hasName = input.readInt() == 1;
        if (hasName) {
            name = input.readString();
        }
        sum = input.readDouble();
    }

    @Override protected String getClassTag() {
        return Result.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel out, int flags) {
        boolean hasName = !StringUtility.isNullOrEmpty(name);
        out.writeInt(hasName ? 1 : 0);
        if (hasName) {
            out.writeString(name);
        }
        out.writeDouble(sum);
    }

    public final static Creator<Result> CREATOR = new Creator<Result>() {

        @Override public Result createFromParcel(Parcel input) {
            return new Result(input);
        }

        @Override public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}