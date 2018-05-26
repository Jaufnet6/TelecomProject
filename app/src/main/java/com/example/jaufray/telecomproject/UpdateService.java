package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class UpdateService extends AppCompatActivity {

    private EditText edtName;
    private EditText edtDescription;
    private EditText edtPrice;

    private String nameEdt;
    private String descriptionEdt;
    private Integer priceEdt;

    private Service service;


    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_edit);

        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra("serviceToModify");

        edtName = (EditText) findViewById(R.id.edt_name_service);
        edtDescription = (EditText) findViewById(R.id.edt_description_name);
        edtPrice = (EditText) findViewById(R.id.edit_price);


        edtName.setText(service.getName(), TextView.BufferType.EDITABLE);
        edtDescription.setText(service.getDescription(), TextView.BufferType.EDITABLE);
        edtPrice.setText(String.valueOf(service.getPrice()), TextView.BufferType.EDITABLE);


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


    //Update the service in the DB
    public void saveServiceUpdate(View view) {


        nameEdt = edtName.getText().toString();
        descriptionEdt = edtDescription.getText().toString();
        priceEdt = Integer.parseInt(edtPrice.getText().toString());

        if(TextUtils.isEmpty(nameEdt)) {
            edtName.setError("Cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(descriptionEdt)) {
            edtDescription.setError("Cannot be empty");
            return;
        }
        if(priceEdt == null){
            Toast.makeText(UpdateService.this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }


        updateService(nameEdt, descriptionEdt, priceEdt);



    }

    //Update the database
     private void updateService(String name, String description, int price)
     {

         mDatabaseReference.child("services").child(service.getId()).child("name").setValue(name);
         mDatabaseReference.child("services").child(service.getId()).child("description").setValue(description);
         mDatabaseReference.child("services").child(service.getId()).child("price").setValue(price);
         Intent intent = new Intent(UpdateService.this, ListServices.class);
         startActivity(intent);
         finish();
     }


    //cancel the update
    public void cancelServiceUpdate(View view) {

        Intent intent = new Intent(UpdateService.this, ListServices.class);
        startActivity(intent);
        this.finish();

    }

    //Method calling the service repository to delete the service
    public void deleteService(Service service){

        mDatabaseReference.child("services").child(service.getId()).removeValue();
        deleteAllJoins(service);
        Intent intent = new Intent(UpdateService.this, ListServices.class);
        startActivity(intent);
        finish();

    }

    private void deleteAllJoins(final Service service) {

        mDatabaseReference.child("packageServiceJoins").addValueEventListener(new ValueEventListener() {
            @Override
            //Go to join table and delete
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final PackageServiceJoin packageServiceJoin = postSnapshot.getValue(PackageServiceJoin.class);
                    if(packageServiceJoin.serviceID.equals(service.getId())){
                        mDatabaseReference.child("packageServiceJoins").child(packageServiceJoin.getId()).removeValue();

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LoadPost:onCancelled", databaseError.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(UpdateService.this, DetailsService.class);
        intent1.putExtra("DetailsService", service);
        startActivity(new Intent(intent1));
        finish();
    }



}
