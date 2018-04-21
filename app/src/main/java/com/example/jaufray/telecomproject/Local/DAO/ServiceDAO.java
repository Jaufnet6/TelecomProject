package com.example.jaufray.telecomproject.Local.DAO;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public interface ServiceDAO {

    //Get Service with specific ID
    @Query("SELECT * FROM services WHERE idService = :serviceId")
    Flowable<Service> getServiceById(int serviceId);


    //Get all services
    @Query("SELECT * FROM services")
    Flowable<List<Service>> getAllServices();

    @Insert
    void insertService(Service... services);

    @Update
    void updateService(Service... services);

    @Delete
    void deleteService(Service service);




}
