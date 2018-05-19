package com.example.jaufray.telecomproject.Model;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.io.Serializable;


public class PackageServiceJoin implements Serializable {

    public String id;

    public final String packageID;


    public final String serviceID;

    public PackageServiceJoin(String id, final String packageID, final String serviceID) {
        this.id = id;
        this.packageID = packageID;
        this.serviceID = serviceID;
    }
    public String getId() {
        return id;
    }

}
