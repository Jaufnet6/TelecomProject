package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;

import java.util.List;

import io.reactivex.Flowable;

public class ClientRepository implements IClientDataSource {

    private IClientDataSource mLocalDataSource;

    private static ClientRepository mInstance;

    public ClientRepository(IClientDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static synchronized ClientRepository getInstance(IClientDataSource mLocalDataSource){
        if(mInstance == null){
            mInstance = new ClientRepository(mLocalDataSource);
        }
        return  mInstance;
    }

    @Override
    public Flowable<Client> getClientById(int clientId) {
        return mLocalDataSource.getClientById(clientId);
    }

    @Override
    public Flowable<List<Client>> getAllClients() {
        return mLocalDataSource.getAllClients();
    }

    @Override
    public Flowable<Package> getPackageName(int packageid) {
        return mLocalDataSource.getPackageName(packageid);
    }

    @Override
    public void insertClient(Client... clients) {
        mLocalDataSource.insertClient(clients);
    }

    @Override
    public void updateClient(Client... clients) {
        mLocalDataSource.updateClient(clients);
    }

    @Override
    public void deleteClient(Client client) {
        mLocalDataSource.deleteClient(client);
    }
}
