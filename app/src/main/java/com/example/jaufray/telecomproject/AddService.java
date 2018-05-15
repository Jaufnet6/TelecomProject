package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.UUID;

import io.reactivex.functions.Consumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class AddService extends AppCompatActivity  {



    private EditText nameService;
    private EditText descriptionService;
    private EditText priceService;

    private String serviceName;
    private String serviceDescription;
    private Integer servicePrice;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_create);

        Intent intent = getIntent();

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

    //save new service
    public void saveService(View view) {

        nameService = (EditText) findViewById(R.id.et_name_service);
        descriptionService = (EditText) findViewById(R.id.et_description_service);
        priceService = (EditText) findViewById(R.id.et_price_service);

        serviceName = nameService.getText().toString();
        serviceDescription = descriptionService.getText().toString();
        try{
            servicePrice = Integer.parseInt(priceService.getText().toString());
        } catch (NumberFormatException ex){
            servicePrice = 0;
        }


        if(TextUtils.isEmpty(serviceName)) {
            nameService.setError("Cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(serviceDescription)) {
            descriptionService.setError("Cannot be empty");
            return;
        }
        if(servicePrice == 0){
            priceService.setError("You have to choose a price");
            return;
        }


        Service service = new Service(UUID.randomUUID().toString(),serviceName,serviceDescription,servicePrice);
        //add le service
        mDatabaseReference.child("services").child(service.getId()).setValue(service);
        Intent intent = new Intent(AddService.this, ListServices.class);
        startActivity(intent);
        this.finish();



    }
    //cancel new service
    public void cancelServiceAdd(View view) {

        Intent intent = new Intent(AddService.this, ListServices.class);
        startActivity(intent);
        finish();

    }



}
