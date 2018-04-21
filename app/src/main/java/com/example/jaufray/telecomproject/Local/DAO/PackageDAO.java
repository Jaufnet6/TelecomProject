package com.example.jaufray.telecomproject.Local.DAO;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jaufray.telecomproject.Model.Package;

import java.util.List;

import io.reactivex.Flowable;

public interface PackageDAO {

    //Get package with specific ID
    @Query("SELECT * FROM packages WHERE idPackage  = :packageId")
    Flowable<Package> getPackageById(int packageId);

    //Get all packages
    @Query("SELECT * FROM packages")
    Flowable<List<Package>> getAllPackages();

    @Insert
    void insertPackage(Package... packages);

    @Update
    void updatePackage(Package... packages);

    @Delete
    void deletePackage(Package packageToDelete);


}
