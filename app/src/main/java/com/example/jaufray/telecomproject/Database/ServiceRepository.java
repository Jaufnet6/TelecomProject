package com.example.jaufray.telecomproject.Database;

import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public class ServiceRepository implements IServiceDataSource {

    private IServiceDataSource mLocalDataSource;

    private static ServiceRepository mInstance;

    public ServiceRepository(IServiceDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static synchronized ServiceRepository getInstance(IServiceDataSource mLocalDataSource){
        if(mInstance == null){
            mInstance = new ServiceRepository(mLocalDataSource);
        }
        return  mInstance;
    }

    @Override
    public Flowable<Service> getServiceById(int serviceId) {
        return mLocalDataSource.getServiceById(serviceId);
    }

    @Override
    public Flowable<List<Service>> getAllServices() {
        return mLocalDataSource.getAllServices();
    }

    @Override
    public void insertService(Service... services) {
        mLocalDataSource.insertService(services);
    }

    @Override
    public void updateService(Service... services) {
        mLocalDataSource.updateService(services);
    }

    @Override
    public void deleteService(Service service) {
        mLocalDataSource.deleteService(service);
    }
}
