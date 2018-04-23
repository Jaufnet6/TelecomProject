package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ServiceRepository;
import com.example.jaufray.telecomproject.Local.ServiceDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Service;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailsService extends Activity{

    private TextView serviceName;
    private TextView serviceDescription;
    private TextView servicePrice;

    private String nameService;
    private String descriptionService;
    private String priceService;

    private Service service;

    //Database
    private CompositeDisposable compositeDisposable;
    private ServiceRepository serviceRepository;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_description);

        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra("DetailsService");

        serviceName = (TextView) findViewById(R.id.id_service_name);
        serviceDescription = (TextView) findViewById(R.id.id_service_details);
        servicePrice = (TextView) findViewById(R.id.id_service_price);

        serviceName.setText(service.getName());
        serviceDescription.setText(service.getDescription());
        servicePrice.setText(String.valueOf(service.getPrice()));

        // Init
        compositeDisposable = new CompositeDisposable();

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        serviceRepository = ServiceRepository.getInstance(ServiceDataSource.getInstance(telecomDatabase.serviceDAO()));


    }

    public void deleteServiceDetails(View view){

        new AlertDialog.Builder(DetailsService.this)
                .setMessage("Do you want to delete ? " + service.toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteService(service);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

    }

    public void editServiceDetails(View view){

        Intent intent = new Intent(DetailsService.this, UpdateService.class);
        intent.putExtra("serviceToModify", service);
        startActivity(intent);

    }

    private void deleteService(final Service service) {

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
                                Toast.makeText(DetailsService.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(DetailsService.this, ListServices.class);
                                startActivity(intent);
                            }
                        }

                );

        compositeDisposable.add(disposable);
        this.finish();

    }


}
