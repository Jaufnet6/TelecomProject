package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ServiceRepository;
import com.example.jaufray.telecomproject.Local.ServiceDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Service;


import java.util.List;

import io.reactivex.functions.Consumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class AddService extends Activity {

    private EditText nameService;
    private EditText descriptionService;
    private EditText priceService;

    private String serviceName;
    private String serviceDescription;
    private Integer servicePrice;

    private Service service;

    private ServiceRepository serviceRepository;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_create);

        Intent intent = getIntent();


    }


    public void saveService(View view) {

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        serviceRepository = ServiceRepository.getInstance(ServiceDataSource.getInstance(telecomDatabase.serviceDAO()));


        nameService = (EditText) findViewById(R.id.et_name_service);
        descriptionService = (EditText) findViewById(R.id.et_description_service);
        priceService = (EditText) findViewById(R.id.et_price_service);

        serviceName = nameService.getText().toString();
        serviceDescription = descriptionService.getText().toString();
        try{
            servicePrice = Integer.parseInt(priceService.getText().toString());
        } catch (NumberFormatException ex){
            servicePrice = 0;
        }


        if(TextUtils.isEmpty(serviceName)) {
            nameService.setError("Cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(serviceDescription)) {
            descriptionService.setError("Cannot be empty");
            return;
        }
        if(servicePrice == 0){
            priceService.setError("You have to choose a price");
            return;
        }



        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                service = new Service(serviceName, serviceDescription, servicePrice);

                serviceRepository.insertService(service);
                e.onComplete();
            }

        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(AddService.this, "Service added!", Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(AddService.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ,

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(AddService.this, ListServices.class);
                                startActivity(intent);
                            }
                        }
                );
        this.finish();
    }

    public void cancelServiceAdd(View view) {

        Intent intent = new Intent(AddService.this, ListServices.class);
        startActivity(intent);
        this.finish();

    }



}
