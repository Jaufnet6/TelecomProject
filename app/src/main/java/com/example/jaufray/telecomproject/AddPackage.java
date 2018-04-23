package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddPackage extends Activity {

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

        //add service to list
        servicesList = (ArrayList<Service>) intent.getSerializableExtra("serviceForPackage");

        if(servicesList == null){
            servicesList = new ArrayList<Service>();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, servicesList);
        registerForContextMenu(listServices);
        listServices.setAdapter(adapter);

    }

    public void openListServices(View view){

        Intent intent = new Intent(AddPackage.this, ListServiceForPackage.class);
        intent.putExtra("serviceForPackage", (Serializable) servicesList);
        startActivity(intent);
        finish();

    }


    public void savePackage(View view) {

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));
        packageServiceJoinRepository = PackageServiceJoinRepository.getInstance((IPackageServiceJoinDataSource) PackageServiceJoinDataSource.getInstance(telecomDatabase.packageServiceJoinDAO()));

        namePackage = (EditText) findViewById(R.id.et_name_package);
        pricePackage = (EditText) findViewById(R.id.et_price_package);

        packageName = namePackage.getText().toString();
        packagePrice = Integer.parseInt(pricePackage.getText().toString());

        if(TextUtils.isEmpty(packageName)) {
            namePackage.setError("Cannot be empty");
            return;
        }

        if(packagePrice == null){
            Toast.makeText(AddPackage.this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }



        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Package pack = new Package(packageName, packagePrice);
                packageRepository.insertPackage(pack);

                for(Service s : servicesList){
                    PackageServiceJoin packServ = new PackageServiceJoin(pack.getId(), s.getId());
                    packageServiceJoinRepository.insert(packServ);
                }
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
                                Intent intent = new Intent(AddPackage.this, ListPackages.class);
                                startActivity(intent);
                            }
                        }
                );
        this.finish();
    }

    public void linkServicePackage(Package pack){

    }



    public void cancelPackageAdd(View view) {

        Intent intent = new Intent(AddPackage.this, ListPackages.class);
        startActivity(intent);
        this.finish();
    }


}
