package com.example.jaufray.telecomproject.Model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

//@Entity(tableName = "services")
public class Service implements Serializable {


    private String id;

    private String name;

    private String description;

    private int price;

    public Service(){

    }
    
    public Service(String UID, String name, String description, int price) {
        this.id = UID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

  //  @Override
    public String toString() {
        return name;
    }


}
