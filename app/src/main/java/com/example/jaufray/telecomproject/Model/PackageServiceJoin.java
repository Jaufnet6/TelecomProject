package com.example.jaufray.telecomproject.Model;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.io.Serializable;

@Entity(tableName = "package_service_join",
        primaryKeys = { "packageJoinID", "serviceJoinID" },
        foreignKeys = {
                @ForeignKey(entity = Package.class,
                        parentColumns = "idPackage",
                        childColumns = "packageJoinID"),
                @ForeignKey(entity = Service.class,
                        parentColumns = "idService",
                        childColumns = "serviceJoinID")
        })
public class PackageServiceJoin implements Serializable {


    @ColumnInfo(name = "packageJoinID")
    public final int packageID;

    @ColumnInfo(name = "serviceJoinID")
    public final int serviceID;

    public PackageServiceJoin(final int packageID, final int serviceID) {
        this.packageID = packageID;
        this.serviceID = serviceID;
    }


}
