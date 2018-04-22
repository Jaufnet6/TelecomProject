package com.example.jaufray.telecomproject.Model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "clients",
        foreignKeys = {
                @ForeignKey(entity = Package.class,
                        parentColumns = "idPackage",
                        childColumns = "idpackage",
                        onDelete = ForeignKey.CASCADE),
                        }
                        , indices = {@Index(value = {"idpackage"})}

)
public class Client {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idClient")
    private int id;

    @ColumnInfo(name = "firstname")
    private String firstname;

    @ColumnInfo(name = "lastname")
    private String lastname;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "npa")
    private int npa;

    @ColumnInfo(name = "locality")
    private String locality;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "idpackage")
    private int idPackage;

    public Client(){

    }

    @Ignore
    public Client(String firstname, String lastname, String phone, String address, int npa, String locality, String country, int idPackage) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.address = address;
        this.npa = npa;
        this.locality = locality;
        this.country = country;
        this.idPackage = idPackage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public int getNpa() {
        return npa;
    }

    public void setNpa(int npa) {
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

    public int getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(int idPackage) {
        this.idPackage = idPackage;
    }




    @Override
    public String toString() {
        return new StringBuilder(firstname).append("\n").append(lastname).append("\n").append(phone)
                .append("\n").append(address).append("\n").append(npa)
                .append("\n").append(locality).append("\n").append(country)
                .append("\n").append(idPackage).toString();
    }
}
