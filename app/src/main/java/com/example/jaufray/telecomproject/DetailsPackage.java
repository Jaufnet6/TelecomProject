package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Database.PackageServiceJoinRepository;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.PackageServiceJoinDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by danie on 24.04.2018.
 */

public class DetailsPackage extends AppCompatActivity {

    
    private TextView packageName;
    private TextView packagePrice;
    private ListView service_List;

    private String namePackage;
    private String pricePackage;
    private List<Service> listOfServices;
    private ArrayAdapter adapter;
    private Package packages;

    private CompositeDisposable compositeDisposable;
    private PackageRepository packageRepository;
    private PackageServiceJoinRepository packageServiceJoinRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_description);

        Intent intent = getIntent();
        packages = (Package) intent.getSerializableExtra("DetailPackages");

        packageName = (TextView) findViewById(R.id.package_title);
        packagePrice = (TextView) findViewById(R.id.package_price);
        service_List = (ListView) findViewById(R.id.list_of_services_in_package);

        packageName.setText(packages.getName());
        packagePrice.setText(String.valueOf(packages.getPrice()) + " CHF");

        listOfServices = new ArrayList<Service>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfServices);
        registerForContextMenu(service_List);
        service_List.setAdapter(adapter);

        // Init
        compositeDisposable = new CompositeDisposable();

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));
        packageServiceJoinRepository = PackageServiceJoinRepository.getInstance(PackageServiceJoinDataSource.getInstance(telecomDatabase.packageServiceJoinDAO()));



        loadData();



    }

    private void loadData() {
        //Use RxJava
        Disposable disposable = packageServiceJoinRepository.getServicesForPackage(packages.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Service>>() {
                               @Override
                               public void accept(List<Service> services) throws Exception{
                                   onGetAllServiceSuccess(services);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(DetailsPackage.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllServiceSuccess(List<Service> services) {
        listOfServices.clear();
        listOfServices.addAll(services);
        adapter.notifyDataSetChanged();

    }

    public void editPackage(View view){

        Intent intent = new Intent(DetailsPackage.this, UpdatePackage.class);
        intent.putExtra("packageToModify", packages);
        intent.putExtra("serviceForPackage", (Serializable) listOfServices);
        startActivity(intent);
        this.finish();

    }

    public void deletePackage(View view){

        new AlertDialog.Builder(DetailsPackage.this)
                .setMessage("Do you want to delete ? " + packages.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePackage();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();


    }

    private void deletePackage(){

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {


            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                deleteLinkPackageToService();
                try{
                    Thread.sleep(50);
                }catch (Exception ex){

                }
                packageRepository.deletePackage(packages);
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
                                Toast.makeText(DetailsPackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(DetailsPackage.this, ListPackages.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                );

        compositeDisposable.add(disposable);

    }

    private void deleteLinkPackageToService() {

        for(Service s : listOfServices){

            final PackageServiceJoin packServ = new PackageServiceJoin(packages.getId(), s.getId());

            Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {


                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    packageServiceJoinRepository.deletePackageServiceJoin(packServ);
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
                                    Toast.makeText(DetailsPackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },

                            new Action() {
                                @Override
                                public void run() throws Exception {
                                }
                            }

                    );

            compositeDisposable.add(disposable);

        }


    }



}
