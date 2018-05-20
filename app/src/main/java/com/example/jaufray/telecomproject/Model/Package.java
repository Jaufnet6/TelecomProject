package com.example.jaufray.telecomproject.Model;

import java.io.Serializable;

public class Package implements Serializable {

    private String id;


    private String name;


    private int price;

    public Package(){

    }

    public Package(String UID, String name, int price) {
        this.id = UID;
        this.name = name;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return name;
    }
}
