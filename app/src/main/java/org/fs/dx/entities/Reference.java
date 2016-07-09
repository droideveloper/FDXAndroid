package org.fs.dx.entities;

import android.os.Parcel;

import com.j256.ormlite.field.DatabaseField;

import org.fs.core.AbstractApplication;
import org.fs.core.AbstractEntity;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.entities.Reference
 */
public final class Reference extends AbstractEntity {

    @DatabaseField(generatedId =  true)
    private Long id;

    @DatabaseField(columnName = TableUtilities.CLM_MOLECULE_ID)
    private Long   moleculeId;

    @DatabaseField(columnName = TableUtilities.CLM_ELEMENT_ID)
    private Long   elementId;

    @DatabaseField(columnName = TableUtilities.CLM_PERCENTAGE)
    private double percentage;

    public Long getId() {
        return id;
    }

    public double getPercentage() {
        return percentage;
    }

    public Long getElementId() {
        return elementId;
    }

    public Long getMoleculeId() {
        return moleculeId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public void setMoleculeId(Long moleculeId) {
        this.moleculeId = moleculeId;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reference() { }
    public Reference(Parcel input) {
        super(input);
    }

    @Override protected void readParcel(Parcel input) {
        moleculeId = input.readLong();
        elementId = input.readLong();
        percentage = input.readDouble();
        id = input.readLong();
    }

    @Override protected String getClassTag() {
        return Reference.class.getSimpleName();
    }

    @Override protected boolean isLogEnabled() {
        return AbstractApplication.isDebug();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel out, int flags) {
        out.writeLong(moleculeId);
        out.writeLong(elementId);
        out.writeDouble(percentage);
        out.writeLong(id);
    }

    public final static Creator<Reference> CREATOR = new Creator<Reference>() {

        @Override public Reference createFromParcel(Parcel input) {
            return new Reference(input);
        }

        @Override public Reference[] newArray(int size) {
            return new Reference[size];
        }
    };
}