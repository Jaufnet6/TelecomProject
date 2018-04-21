package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.Client;

import java.util.List;

import io.reactivex.Flowable;

public interface IClientDataSource {

    Flowable<Client> getClientById(int clientId);
    Flowable<List<Client>> getAllClients();
    void insertClient(Client... clients);
    void updateClient(Client... clients);
    void deleteClient(Client client);


}
