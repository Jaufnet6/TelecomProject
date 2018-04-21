package com.example.jaufray.telecomproject.Database;


import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public interface IServiceDataSource {

    Flowable<Service> getServiceById(int serviceId);
    Flowable<List<Service>> getAllServices();
    void insertService(Service... services);
    void updateService(Service... services);
    void deleteService(Service service);


}
