package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.IPackageServiceJoinDataSource;
import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Database.PackageServiceJoinRepository;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.PackageServiceJoinDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddPackage extends AppCompatActivity {

    private EditText namePackage;
    private EditText pricePackage;

    private String packageName;
    private Integer packagePrice;

    //private Service service;
    private List<Service> servicesList;

    private ListView listServices;
    ArrayAdapter adapter;

    private PackageRepository packageRepository;
    private PackageServiceJoinRepository packageServiceJoinRepository;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_create);
        Intent intent = getIntent();

        listServices = (ListView) findViewById(R.id.full_list_service_package);


        namePackage = (EditText) findViewById(R.id.et_name_package);
        pricePackage = (EditText) findViewById(R.id.et_price_package);

        //add service to list
        servicesList = (ArrayList<Service>) intent.getSerializableExtra("serviceForPackage");
        packageName = (String) intent.getStringExtra("packageName");
        packagePrice = (Integer) intent.getIntExtra("packagePrice", 0);

        if(servicesList == null){
            servicesList = new ArrayList<Service>();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, servicesList);
        registerForContextMenu(listServices);
        listServices.setAdapter(adapter);

    }

    public void openListServices(View view){


        packageName = namePackage.getText().toString();

        try{
            packagePrice = Integer.parseInt(pricePackage.getText().toString());
        } catch (NumberFormatException ex){
            packagePrice = 0;
        }


        Intent intent = new Intent(AddPackage.this, ListServiceForPackage.class);
        intent.putExtra("serviceForPackage", (Serializable) servicesList);
        intent.putExtra("packageName", packageName);
        intent.putExtra("packagePrice", packagePrice);
        startActivity(intent);
        finish();

    }


    public void savePackage(View view) {

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));
        packageServiceJoinRepository = PackageServiceJoinRepository.getInstance((IPackageServiceJoinDataSource) PackageServiceJoinDataSource.getInstance(telecomDatabase.packageServiceJoinDAO()));


        packageName = namePackage.getText().toString();

        try{
            packagePrice = Integer.parseInt(pricePackage.getText().toString());
        } catch (NumberFormatException ex){
            int total = 0;
            for(Service s : servicesList)
                total =+ s.getPrice();


            packagePrice = total;
        }



        if(TextUtils.isEmpty(packageName)) {
            namePackage.setError("Cannot be empty");
            return;
        }

        if(packagePrice == 0){
            Toast.makeText(AddPackage.this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }



        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Package pack = new Package(packageName, packagePrice);
                packageRepository.insertPackage(pack);
                e.onComplete();
                addDataLinkService();
            }

        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(AddPackage.this, "Package added!", Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(AddPackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ,

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(AddPackage.this, ListPackages.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                );

        this.finish();
    }

    public void addDataLinkService(){

        int id = retrieveLastIDPackage();


        for(Service s : servicesList){
            final PackageServiceJoin packServ = new PackageServiceJoin(id, s.getId());


            Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    packageServiceJoinRepository.insert(packServ);
                    e.onComplete();
                }

            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer() {
                                   @Override
                                   public void accept(Object o) throws Exception {
                                       Toast.makeText(AddPackage.this, "Package added!", Toast.LENGTH_SHORT).show();
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(AddPackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            ,

                            new Action() {
                                @Override
                                public void run() throws Exception {
                                }
                            }
                    );

        }
    }

    private int retrieveLastIDPackage() {

        final ArrayList<Package> allPackages = new ArrayList<Package>();

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        //Use RxJava
        Disposable disposable = packageRepository.getAllPackages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Package>>() {
                               @Override
                               public void accept(List<Package> packages) throws Exception{
                                   for(Package p : packages)
                                       allPackages.add(p);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }
                );
        compositeDisposable.add(disposable);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int size = allPackages.size();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int id = allPackages.get(size-1).getId();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id;


    }


    public void cancelPackageAdd(View view) {

        Intent intent = new Intent(AddPackage.this, ListPackages.class);
        startActivity(intent);
        this.finish();
    }


}
