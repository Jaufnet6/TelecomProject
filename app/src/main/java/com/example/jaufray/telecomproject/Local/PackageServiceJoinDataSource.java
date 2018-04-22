package com.example.jaufray.telecomproject.Local;

import com.example.jaufray.telecomproject.Local.DAO.PackageServiceJoinDAO;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public class PackageServiceJoinDataSource implements PackageServiceJoinDAO{


    private PackageServiceJoinDAO packageServiceJoinDAO;

    private static PackageServiceJoinDataSource mInstance;

    public PackageServiceJoinDataSource(PackageServiceJoinDAO packageServiceJoinDAO) {
        this.packageServiceJoinDAO = packageServiceJoinDAO;
    }

    public synchronized static PackageServiceJoinDataSource getInstance(PackageServiceJoinDAO packageServiceJoinDAO){
        if(mInstance == null){
            mInstance = new PackageServiceJoinDataSource(packageServiceJoinDAO);
        }
        return  mInstance;
    }


    @Override
    public void insert(PackageServiceJoin packageServiceJoin) {

    }

    @Override
    public Flowable<List<Service>> getServicesForPackage(int packageId) {
        return null;
    }
}
