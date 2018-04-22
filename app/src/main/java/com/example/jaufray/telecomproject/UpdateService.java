package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ServiceRepository;
import com.example.jaufray.telecomproject.Local.ServiceDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Service;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class UpdateService extends Activity{

    private EditText edtName;
    private EditText edtDescription;
    private EditText edtPrice;

    private String nameEdt;
    private String descriptionEdt;
    private Integer priceEdt;

    private Service service;

    private CompositeDisposable compositeDisposable;
    private ServiceRepository serviceRepository;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_edit);

        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra("serviceToModify");

        edtName = (EditText) findViewById(R.id.edt_name_service);
        edtDescription = (EditText) findViewById(R.id.edt_description_name);
        edtPrice = (EditText) findViewById(R.id.edit_price);


        edtName.setText(service.getName(), TextView.BufferType.EDITABLE);
        edtDescription.setText(service.getDescription(), TextView.BufferType.EDITABLE);
        edtPrice.setText(String.valueOf(service.getPrice()), TextView.BufferType.EDITABLE);


    }



    public void saveServiceUpdate(View view) {

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        serviceRepository = ServiceRepository.getInstance(ServiceDataSource.getInstance(telecomDatabase.serviceDAO()));



        nameEdt = edtName.getText().toString();
        descriptionEdt = edtDescription.getText().toString();
        priceEdt = Integer.parseInt(edtPrice.getText().toString());

        if(TextUtils.isEmpty(nameEdt)) {
            edtName.setError("Cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(descriptionEdt)) {
            edtDescription.setError("Cannot be empty");
            return;
        }
        if(priceEdt == null){
            Toast.makeText(UpdateService.this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }



        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                service.setName(nameEdt);
                service.setDescription(descriptionEdt);
                service.setPrice(priceEdt);

                serviceRepository.updateService(service);
                e.onComplete();
            }

        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(UpdateService.this, "Service updated!", Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(UpdateService.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ,

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(UpdateService.this, ListServices.class);
                                startActivity(intent);
                            }
                        }
                );
        this.finish();
    }


    public void cancelServiceUpdate(View view) {

        Intent intent = new Intent(UpdateService.this, ListServices.class);
        startActivity(intent);
        this.finish();

    }

    public void deleteService(View view){

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                serviceRepository.deleteService(service);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           },

                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(UpdateService.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(UpdateService.this, ListServices.class);
                                startActivity(intent);

                            }
                        }

                );

        compositeDisposable.add(disposable);
        this.finish();

    }


}
