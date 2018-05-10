package com.example.jaufray.telecomproject.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

//@Entity(tableName = "packages")
public class Package implements Serializable {


   /* @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPackage")*/
    private int id;

   // @ColumnInfo(name = "name")
    private String name;

   // @ColumnInfo(name = "price")
    private int price;

    public Package(){

    }

  //  @Ignore
    public Package(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

   // @Override
    public String toString() {
        return name;
    }
}
