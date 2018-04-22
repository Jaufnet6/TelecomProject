package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public interface IPackageServiceJoinDataSource {

    void insert(PackageServiceJoin packageServiceJoin);
    Flowable<List<Service>> getServicesForPackage(final int packageId);



}
