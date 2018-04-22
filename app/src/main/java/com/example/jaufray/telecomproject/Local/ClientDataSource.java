package com.example.jaufray.telecomproject.Local;

import com.example.jaufray.telecomproject.Database.IClientDataSource;
import com.example.jaufray.telecomproject.Local.DAO.ClientDAO;
import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;

import java.util.List;

import io.reactivex.Flowable;

public class ClientDataSource implements IClientDataSource {

    private ClientDAO clientDAO;

    private static ClientDataSource mInstance;

    public ClientDataSource(ClientDAO clientDAO){
        this.clientDAO = clientDAO;
    }

    public synchronized static ClientDataSource getInstance(ClientDAO clientDAO){
        if(mInstance == null){
            mInstance = new ClientDataSource(clientDAO);
        }
        return  mInstance;
    }


    @Override
    public Flowable<Client> getClientById(int clientId) {
        return clientDAO.getClientById(clientId);
    }

    @Override
    public Flowable<List<Client>> getAllClients() {
        return clientDAO.getAllClients();
    }

    @Override
    public Flowable<Package> getPackageName(int packageid) {
        return clientDAO.getPackageName(packageid);
    }

    @Override
    public void insertClient(Client... clients) {
        clientDAO.insertClient(clients);
    }

    @Override
    public void updateClient(Client... clients) {
        clientDAO.updateClient(clients);
    }

    @Override
    public void deleteClient(Client client) {
        clientDAO.deleteClient(client);
    }
}
