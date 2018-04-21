package com.example.jaufray.telecomproject.Local;

import com.example.jaufray.telecomproject.Database.IPackageDataSource;
import com.example.jaufray.telecomproject.Local.DAO.PackageDAO;
import com.example.jaufray.telecomproject.Model.Package;

import java.util.List;

import io.reactivex.Flowable;

public class PackageDataSource implements IPackageDataSource {

    private PackageDAO packageDAO;

    private static PackageDataSource mInstance;

    public PackageDataSource(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    public synchronized static PackageDataSource getInstance(PackageDAO packageDAO){
        if(mInstance == null){
            mInstance = new PackageDataSource(packageDAO);
        }
        return  mInstance;
    }

    @Override
    public Flowable<Package> getPackageById(int packageId) {
        return packageDAO.getPackageById(packageId);
    }

    @Override
    public Flowable<List<Package>> getAllPackages() {
        return packageDAO.getAllPackages();
    }

    @Override
    public void insertPackage(Package... packages) {
        packageDAO.insertPackage(packages);
    }

    @Override
    public void updatePackage(Package... packages) {
        packageDAO.updatePackage(packages);
    }

    @Override
    public void deletePackage(Package packageToDelete) {
        packageDAO.deletePackage(packageToDelete);
    }
}
