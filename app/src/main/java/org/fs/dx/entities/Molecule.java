package org.fs.dx.entities;

import android.os.Parcel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractEntity;
import org.fs.util.StringUtility;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.entities.Molecule
 */
@DatabaseTable(tableName = TableUtilities.TABLE_MOLECULE)
public final class Molecule extends AbstractEntity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(columnName = TableUtilities.CLM_NAME)
    private String name;

    @DatabaseField(columnName = TableUtilities.CLM_PRICE)
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Molecule() { }
    public Molecule(Parcel input) {
        super(input);
    }

    @Override protected void readParcel(Parcel input) {
        boolean hasName = input.readInt() == 1;
        if (hasName) {
            name = input.readString();
        }
        price = input.readDouble();
        id = input.readLong();
    }

    @Override protected String getClassTag() {
        return Molecule.class.getSimpleName();
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
        out.writeDouble(price);
        out.writeLong(id);
    }

    public final static Creator<Molecule> CREATOR = new Creator<Molecule>() {

        @Override public Molecule createFromParcel(Parcel input) {
            return new Molecule(input);
        }

        @Override public Molecule[] newArray(int size) {
            return new Molecule[size];
        }
    };
}