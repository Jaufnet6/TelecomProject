package com.example.jaufray.telecomproject.Local.DAO;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jaufray.telecomproject.Model.Client;

import java.util.List;

import io.reactivex.Flowable;

public interface ClientDAO {

    //Get client with specific ID
    @Query("SELECT * FROM clients WHERE idClient = :clientId")
    Flowable<Client> getClientById(int clientId);


    //Get all clients
    @Query("SELECT * FROM clients")
    Flowable<List<Client>> getAllClients();

    @Insert
    void insertClient(Client... clients);

    @Update
    void updateClient(Client... clients);

    @Delete
    void deleteClient(Client client);



}
