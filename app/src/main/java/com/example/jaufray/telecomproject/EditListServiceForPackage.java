package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Package;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
//Class to chosose services for 1 package
public class EditListServiceForPackage extends AppCompatActivity {

    private ListView list_services;



    //Adapter
    private List<Service> serviceList = new ArrayList<Service>();
    private ArrayAdapter adapter;
    private List<Service> listToPass;

    private Package packages;

    private String namePackage;
    private Integer pricePackage;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


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




        // Init View
        list_services = (ListView) findViewById(R.id.service_list_package);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        registerForContextMenu(list_services);
        list_services.setAdapter(adapter);


        //Firebase
        initFirebase();
        addFirebaseListener();


        //Click on one service
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

    //Get list of services the user can choose for the package
    private void addFirebaseListener() {
        mDatabaseReference.child("services").addValueEventListener(new ValueEventListener() {
            @Override
            //Retrieve data from firebase
            //DataSnapShot : contient les données provenant d'un emplacement de bd Firebase - on recoit les données en tant que DataSnapShot
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (serviceList.size() > 0) {
                    serviceList.clear();
                }

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service service = postSnapshot.getValue(Service.class);
                    serviceList.add(service);
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LoadPost:onCancelled", databaseError.toException());
            }
        });
    }

    //Firebase initialization
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Get firebase instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }


}
