package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ServiceRepository;
import com.example.jaufray.telecomproject.Local.ServiceDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditListServiceForPackage extends AppCompatActivity {

    private ListView list_services;

    //Database
    private CompositeDisposable compositeDisposable;
    private ServiceRepository serviceRepository;

    //Adapter
    private List<Service> serviceList = new ArrayList<Service>();
    private ArrayAdapter adapter;
    private List<Service> listToPass;

    private Package packages;

    private String namePackage;
    private Integer pricePackage;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_choice_for_package);
        final Intent intent = getIntent();
        listToPass = (ArrayList<Service>) intent.getSerializableExtra("serviceForPackage");
        packages = (Package) intent.getSerializableExtra("packageToEdit");
        namePackage = (String) intent.getStringExtra("packageName");
        pricePackage = (Integer) intent.getIntExtra("packagePrice", 0);




        if(listToPass == null)
            listToPass = new ArrayList<Service>();



        // Init
        compositeDisposable = new CompositeDisposable();

        // Init View
        list_services = (ListView) findViewById(R.id.service_list_package);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        registerForContextMenu(list_services);
        list_services.setAdapter(adapter);

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        serviceRepository = ServiceRepository.getInstance(ServiceDataSource.getInstance(telecomDatabase.serviceDAO()));

        //Load all data from DB
        loadData();


        list_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Service service = (Service)adapterView.getAdapter().getItem(i);
                listToPass.add(service);
                Intent intent1 = new Intent(EditListServiceForPackage.this, UpdatePackage.class);
                intent1.putExtra("serviceForPackage", (Serializable) listToPass);
                intent1.putExtra("packageToModify", packages);
                intent1.putExtra("packageName", namePackage);
                intent1.putExtra("packagePrice", pricePackage);
                startActivity(intent1);
                finish();
            }
        });
    }

    private void loadData() {

        //Use RxJava
        Disposable disposable = serviceRepository.getAllServices()
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
                                Toast.makeText(EditListServiceForPackage.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllServiceSuccess(List<Service> services) {
        serviceList.clear();
        serviceList.addAll(services);
        adapter.notifyDataSetChanged();

    }


}
