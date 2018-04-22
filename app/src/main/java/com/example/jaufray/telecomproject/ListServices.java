package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ServiceRepository;
import com.example.jaufray.telecomproject.Local.ServiceDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Service;

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

public class ListServices extends Activity{

    private ListView list_services;

    //Database
    private CompositeDisposable compositeDisposable;
    private ServiceRepository serviceRepository;

    //Adapter
    List<Service> serviceList = new ArrayList<>();
    ArrayAdapter adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        Intent intent = getIntent();

        // Init
        compositeDisposable = new CompositeDisposable();

        // Init View
        list_services = (ListView) findViewById(R.id.full_service_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        registerForContextMenu(list_services);
        list_services.setAdapter(adapter);

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        serviceRepository = ServiceRepository.getInstance(ServiceDataSource.getInstance(telecomDatabase.serviceDAO()));

        //Load all data from DB
        loadData();

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
                                Toast.makeText(ListServices.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
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


    public void changeToCreateService(View view){
        Intent intent = new Intent(ListServices.this, AddService.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action :");

        menu.add(Menu.NONE,0, Menu.NONE, "Update");
        menu.add(Menu.NONE,1, Menu.NONE, "Delete");




    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Service service = serviceList.get(info.position);

        switch (item.getItemId())
        {
            case 0: //Update
            {
                Intent intent = new Intent(ListServices.this, UpdateService.class);
                intent.putExtra("serviceToModify", service);
                startActivity(intent);

            }
            break;
            case 1: //Delete
            {
                new AlertDialog.Builder(ListServices.this)
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
            break;
        }
        return true;
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
                                Toast.makeText(ListServices.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }

                );

        compositeDisposable.add(disposable);

    }

}
