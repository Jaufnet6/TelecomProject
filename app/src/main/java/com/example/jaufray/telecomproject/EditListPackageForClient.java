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


import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//class to choose 1 package for a client
public class EditListPackageForClient extends AppCompatActivity {

    private ListView list_package;
    //Database
    private CompositeDisposable compositeDisposable;

    //Adapter
    private List<Package> packageList = new ArrayList<Package>();
    private ArrayAdapter adapter;

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private String clientNPA;
    private String clientLocality;
    private String clientCountry;

    private Client client;
    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_choice_for_client);
        final Intent intent = getIntent();
        //intent.putExtra("PackageForClient", (Serializable) packages);

        compositeDisposable = new CompositeDisposable();

        //Init View
        list_package = (ListView) findViewById(R.id.package_list_client);

        //Retrieve user inputs from creation frame
        clientName = (String) intent.getStringExtra("clientName");
        clientPhone = (String) intent.getStringExtra("clientPhone");
        clientAddress = (String) intent.getStringExtra("clientAddress");
        clientNPA = (String) intent.getStringExtra("clientNPA");
        clientLocality = (String) intent.getStringExtra("clientLocality");
        clientCountry = (String) intent.getStringExtra("clientCountry");
        client = (Client) intent.getSerializableExtra("clientToModify");



        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, packageList);
        registerForContextMenu(list_package);
        list_package.setAdapter(adapter);


        //Firebase
        initFirebase();
        addFirebaseListener();


        //Click on 1 package
        list_package.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Package packages = (Package) adapterView.getAdapter().getItem(i);
                Intent intent1 = new Intent(EditListPackageForClient.this, UpdateClient.class);
                intent1.putExtra("packageToClient", packages);
                intent1.putExtra("clientName", clientName);
                intent1.putExtra("clientPhone", clientPhone);
                intent1.putExtra("clientAddress", clientAddress);
                intent1.putExtra("clientNPA", clientNPA);
                intent1.putExtra("clientCountry", clientCountry);
                intent1.putExtra("clientLocality", clientLocality);
                intent1.putExtra("clientToModify", client);
                startActivity(intent1);
                finish();
            }
        });
    }

    private void addFirebaseListener() {
        mDatabaseReference.child("packages").addValueEventListener(new ValueEventListener() {
            @Override
            //Retrieve data from firebase
            //DataSnapShot : contient les données provenant d'un emplacement de bd Firebase - on recoit les données en tant que DataSnapShot
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (packageList.size() > 0) {
                    packageList.clear();
                }

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Package aPackage = postSnapshot.getValue(Package.class);
                    packageList.add(aPackage);
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


    //put packages in arraylist
    private void onGetAllPackageSuccess(List<Package> packages) {
        packageList.clear();
        packageList.addAll(packages);
        adapter.notifyDataSetChanged();
    }


}
