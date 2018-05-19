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

import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


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

        if(packageName != null)
            namePackage.setText(packageName);
        if(packagePrice != null)
            pricePackage.setText(String.valueOf(packagePrice));

        if(servicesList == null){
            servicesList = new ArrayList<Service>();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, servicesList);
        registerForContextMenu(listServices);
        listServices.setAdapter(adapter);

        //Firebase
        initFirebase();
    }

    //Firebase initialization
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Get firebase instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

    }

    //Go and choose service for package
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

    //Save button
    public void savePackage(View view) {

        //Database

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

        Package packages = new Package(UUID.randomUUID().toString(),packageName,packagePrice);
        //add package to database
        mDatabaseReference.child("packages").child(packages.getId()).setValue(packages);
        Intent intent = new Intent(AddPackage.this, ListPackages.class);
        startActivity(intent);
        finish();
        addDataLinkService(packages);
    }

    public void addDataLinkService(Package packages)
    {
        for(Service s : servicesList){
           String idService = s.getId();


        PackageServiceJoin packageServiceJoin = new PackageServiceJoin(UUID.randomUUID().toString(), packages.getId(), s.getId());
        mDatabaseReference.child("packageServiceJoins").child(packageServiceJoin.getId()).setValue(packageServiceJoin);

        }
    }

    //Cancel package creation
    public void cancelPackageAdd(View view) {

        Intent intent = new Intent(AddPackage.this, ListPackages.class);
        startActivity(intent);
        this.finish();
    }


}
