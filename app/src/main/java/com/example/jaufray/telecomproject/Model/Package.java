package com.example.jaufray.telecomproject.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "packages",
        foreignKeys = {
                @ForeignKey(entity = Service.class,
                        parentColumns = "idService",
                        childColumns = "idservice",
                        onDelete = ForeignKey.CASCADE),
        }
        , indices = {@Index(value = {"idservice"})}
)
public class Package {


    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPackage")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "idclient")
    private int idClient;

    @ColumnInfo(name = "idservice")
    private int idService;

    public Package(){

    }

    @Ignore
    public Package(String name, int price, String description, int idClient, int idService) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.idClient = idClient;
        this.idService = idService;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    @Override
    public String toString() {
        return new StringBuilder(name).append("\n").append(price).append("\n").append(description)
                .append("\n").append(idClient).append("\n").append(idService).toString();
    }
}
