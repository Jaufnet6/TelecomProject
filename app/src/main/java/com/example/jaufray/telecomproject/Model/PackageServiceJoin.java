package com.example.jaufray.telecomproject.Model;

import java.io.Serializable;


public class PackageServiceJoin implements Serializable {

    public String id;

    public String packageID;


    public String serviceID;

    public PackageServiceJoin(){

    }

    public PackageServiceJoin(String id, final String packageID, final String serviceID) {
        this.id = id;
        this.packageID = packageID;
        this.serviceID = serviceID;
    }

    public String getId() {
        return id;
    }

}
