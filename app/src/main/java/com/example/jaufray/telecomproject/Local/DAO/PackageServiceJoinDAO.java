package com.example.jaufray.telecomproject.Local.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PackageServiceJoinDAO {


    @Insert
    void insert(PackageServiceJoin packageServiceJoin);

    //Get the names of all services attached to a specific package
    @Query("SELECT name FROM services INNER JOIN package_service_join ON idService = serviceJoinID WHERE  packageJoinID=:packageId")
    Flowable<List<Service>> getServicesForPackage(final int packageId);


}
