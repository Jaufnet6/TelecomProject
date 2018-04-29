package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class ListServices extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView list_services;

    //Database
    private CompositeDisposable compositeDisposable;
    private ServiceRepository serviceRepository;

    //Adapter
    List<Service> serviceList = new ArrayList<>();
    ArrayAdapter adapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        final Intent intent = getIntent();

        //Drawer menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_services);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_services);
        navigationView.setNavigationItemSelectedListener(this);

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

        list_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Service service = (Service)adapterView.getAdapter().getItem(i);

                Intent intent1 = new Intent(ListServices.this, DetailsService.class);
                intent1.putExtra("DetailsService", service);
                startActivity(intent1);
                finish();
            }
        });
    }

    //Drawer Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //Drawer Menu Buttons
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(ListServices.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(ListServices.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Intent intent3 = new Intent(ListServices.this, ListClient.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent5 = new Intent(ListServices.this, MainMenu.class);
                startActivity(intent5);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(ListServices.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Toast.makeText(this, getString(R.string.al_main_service), Toast.LENGTH_LONG).show();
                return true;

            default:
                return false;

        }
    }

    //Retrieve all services from db
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
    //put retrieve services in list connected to adapter and listview
    private void onGetAllServiceSuccess(List<Service> services) {
        serviceList.clear();
        serviceList.addAll(services);
        adapter.notifyDataSetChanged();

    }

    //Add a service button
    public void changeToCreateService(View view){
        Intent intent = new Intent(ListServices.this, AddService.class);
        startActivity(intent);
       finish();
    }

    //Update and delete long click
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action :");

        menu.add(Menu.NONE,0, Menu.NONE, "Update");
        menu.add(Menu.NONE,1, Menu.NONE, "Delete");




    }

    //pressing on delete or update
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
                finish();

            }
            break;
            case 1: //Delete
            {
                new AlertDialog.Builder(ListServices.this)
                        .setMessage("Do you want to delete ? " + service.getName().toString())
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

    //Delete service in DB
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
                                Toast.makeText(ListServices.this, getString(R.string.al_service_delete), Toast.LENGTH_LONG).show();
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
