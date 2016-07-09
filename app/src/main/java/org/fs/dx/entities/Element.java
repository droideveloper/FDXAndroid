package org.fs.dx.entities;

import android.os.Parcel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractEntity;
import org.fs.util.StringUtility;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.entities.Element
 */
@DatabaseTable(tableName = TableUtilities.TABLE_ELEMENT)
public final class Element extends AbstractEntity {

    @DatabaseField(generatedId = true)
    private Long    id;

    @DatabaseField(columnName = TableUtilities.CLM_NAME)
    private String  name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Element() {}
    public Element(Parcel input) {
        super(input);
    }

    @Override protected void readParcel(Parcel input) {
        boolean hasName = input.readInt() == 1;
        if (hasName) {
            name = input.readString();
        }
        id = input.readLong();
    }

    @Override protected String getClassTag() {
        return Element.class.getSimpleName();
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
        out.writeLong(id);
    }

    public final static Creator<Element> CREATOR = new Creator<Element>() {

        @Override public Element createFromParcel(Parcel input) {
            return new Element(input);
        }

        @Override public Element[] newArray(int size) {
            return new Element[size];
        }
    };
}