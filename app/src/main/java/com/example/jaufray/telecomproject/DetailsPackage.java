package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

public class DetailsPackage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private TextView packageName;
    private TextView packagePrice;
    private ListView service_List;

    private List<Service> listOfServices;
    private ArrayAdapter adapter;
    private Package packages;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private DrawerLayout mDrawerLayout;
    //Class to tie the functionnality of DraweLayout and the framework ActionBar
    //to implement the recommended design for navigation drawers
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_description);

        Intent intent = getIntent();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_details_package);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_details_package);
        navigationView.setNavigationItemSelectedListener(this);

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

        initFirebase();

        loadServicesForPackage();



    }

    //Firebase initialization
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Get firebase instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

    }

    //Get all services for the package
    private void loadServicesForPackage() {

        mDatabaseReference.child("packageServiceJoin").addValueEventListener(new ValueEventListener() {
            @Override
            //Retrieve data from firebase
            //DataSnapShot : contient les données provenant d'un emplacement de bd Firebase - on recoit les données en tant que DataSnapShot
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listOfServices.size() > 0) {
                    listOfServices.clear();
                }

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PackageServiceJoin packageServiceJoin = postSnapshot.getValue(PackageServiceJoin.class);
                    if(packageServiceJoin.packageID == packages.getId()){

                        //listOfServices.add(service);
                    }

                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LoadPost:onCancelled", databaseError.toException());
            }
        });

    }

    //Drawer top button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
           return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(DetailsPackage.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(DetailsPackage.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Intent intent6 = new Intent(DetailsPackage.this, ListClient.class);
                startActivity(intent6);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent3 = new Intent(DetailsPackage.this, MainMenu.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(DetailsPackage.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(DetailsPackage.this, ListServices.class);
                startActivity(intent5);
                finish();
                return true;

            default:
                return false;

        }
    }

    //Put services in arraylist
    private void onGetAllServiceSuccess(List<Service> services) {
        listOfServices.clear();
        listOfServices.addAll(services);
        adapter.notifyDataSetChanged();

    }
    //Go to edit view
    public void editPackage(View view){

        Intent intent = new Intent(DetailsPackage.this, UpdatePackage.class);
        intent.putExtra("packageToModify", packages);
        intent.putExtra("serviceForPackage", (Serializable) listOfServices);
        startActivity(intent);
        this.finish();

    }
    //Delete package button
    public void deletePackage(View view){

        new AlertDialog.Builder(DetailsPackage.this)
                .setMessage("Do you want to delete ? " + packages.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteServiceForPackage();
                        deletePackage();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();


    }

    private void deleteServiceForPackage(){

    }

    //Delete from DB
    private void deletePackage(){



    }

    //Delete all rows in join table where idPackage exists
    private void deleteLinkPackageToService() {

        for(Service s : listOfServices){

            final PackageServiceJoin packServ = new PackageServiceJoin(packages.getId(), s.getId());



        }


    }



}
