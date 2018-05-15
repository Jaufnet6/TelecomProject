package com.example.jaufray.telecomproject.Model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

/*@Entity(tableName = "clients",
        foreignKeys = {
                @ForeignKey(entity = Package.class,
                        parentColumns = "idPackage",
                        childColumns = "idpackage",
                        onDelete = ForeignKey.CASCADE),
                        }
                        , indices = {@Index(value = {"idpackage"})}

)*/
public class Client implements Serializable {

    //On enlève tous les @... car c'est spécifique à la Room

  /*  @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idClient")*/

    private String id;

  //  @ColumnInfo(name = "name")
    private String name;

   // @ColumnInfo(name = "phone")
    private String phone;

   // @ColumnInfo(name = "address")
    private String address;

   // @ColumnInfo(name = "npa")
    private String npa;

   // @ColumnInfo(name = "locality")
    private String locality;

    //@ColumnInfo(name = "country")
    private String country;

   // @ColumnInfo(name = "idpackage")
    private String idPackage;

    public Client(){

    }

  //  @Ignore
    public Client(String UID, String name, String phone, String address, String npa, String locality, String country, String idPackage) {
        this.id = UID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.npa = npa;
        this.locality = locality;
        this.country = country;
        this.idPackage = idPackage;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(String idPackage) {
        this.idPackage = idPackage;
    }

//Pas de méthode toMap car on a rien qui est Exclude de notre bd


    @Override
    public String toString() {
        return name;
    }
}
