package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.Package;

import java.util.List;

import io.reactivex.Flowable;

public class PackageRepository implements IPackageDataSource {

    private IPackageDataSource mLocalDataSource;

    private static PackageRepository mInstance;

    public PackageRepository(IPackageDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static synchronized PackageRepository getInstance(IPackageDataSource mLocalDataSource){
        if(mInstance == null){
            mInstance = new PackageRepository(mLocalDataSource);
        }
        return  mInstance;
    }

    @Override
    public Flowable<Package> getPackageById(int packageId) {
        return mLocalDataSource.getPackageById(packageId);
    }

    @Override
    public Flowable<List<Package>> getAllPackages() {
        return mLocalDataSource.getAllPackages();
    }

    @Override
    public void insertPackage(Package... packages) {
        mLocalDataSource.insertPackage(packages);
    }

    @Override
    public void updatePackage(Package... packages) {
        mLocalDataSource.updatePackage(packages);
    }

    @Override
    public void deletePackage(Package packageToDelete) {
        mLocalDataSource.deletePackage(packageToDelete);
    }
}
