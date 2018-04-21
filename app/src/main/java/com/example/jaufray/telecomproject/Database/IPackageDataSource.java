package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.Package;

import java.util.List;

import io.reactivex.Flowable;

public interface IPackageDataSource {

    Flowable<Package> getPackageById(int packageId);
    Flowable<List<Package>> getAllPackages();
    void insertPackage(Package... packages);
    void updatePackage(Package... packages);
    void deletePackage(Package packageToDelete);

}
