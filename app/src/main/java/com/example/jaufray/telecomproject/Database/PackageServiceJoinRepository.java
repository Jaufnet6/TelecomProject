package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public class PackageServiceJoinRepository  implements IPackageServiceJoinDataSource{

    private IPackageServiceJoinDataSource mLocalDataSource;

    private static PackageServiceJoinRepository mInstance;

    public PackageServiceJoinRepository(IPackageServiceJoinDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static synchronized PackageServiceJoinRepository getInstance(IPackageServiceJoinDataSource mLocalDataSource){
        if(mInstance == null){
            mInstance = new PackageServiceJoinRepository(mLocalDataSource);
        }
        return  mInstance;
    }


    @Override
    public void insert(PackageServiceJoin packageServiceJoin) {
        mLocalDataSource.insert(packageServiceJoin);
    }

    @Override
    public Flowable<List<Service>> getServicesForPackage(int packageId) {
        return mLocalDataSource.getServicesForPackage(packageId);
    }
}
