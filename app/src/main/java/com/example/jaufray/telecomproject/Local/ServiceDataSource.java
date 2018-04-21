package com.example.jaufray.telecomproject.Local;

import com.example.jaufray.telecomproject.Database.IServiceDataSource;
import com.example.jaufray.telecomproject.Local.DAO.ServiceDAO;
import com.example.jaufray.telecomproject.Model.Service;

import java.util.List;

import io.reactivex.Flowable;

public class ServiceDataSource implements IServiceDataSource {

    private ServiceDAO serviceDAO;

    private static ServiceDataSource mInstance;

    public ServiceDataSource(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public synchronized static ServiceDataSource getInstance(ServiceDAO serviceDAO){
        if(mInstance == null){
            mInstance = new ServiceDataSource(serviceDAO);
        }
        return  mInstance;
    }

    @Override
    public Flowable<Service> getServiceById(int serviceId) {
        return serviceDAO.getServiceById(serviceId);
    }

    @Override
    public Flowable<List<Service>> getAllServices() {
        return serviceDAO.getAllServices();
    }

    @Override
    public void insertService(Service... services) {
        serviceDAO.insertService(services);
    }

    @Override
    public void updateService(Service... services) {
        serviceDAO.updateService(services);
    }

    @Override
    public void deleteService(Service service) {
        serviceDAO.deleteService(service);
    }
}
