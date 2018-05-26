package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterators;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Iterator;
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

public class UpdatePackage extends AppCompatActivity {

    private EditText edtName;
    private ListView service_list;
    private EditText edtPrice;

    private String namePackage;
    private ArrayList<Service> listService;
    private ArrayList<Service> removedServices = new ArrayList<>();
    private Integer pricePackage;

    private Package packages;

    private ArrayAdapter adapter;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);
        Intent intent = getIntent();

        edtName = (EditText) findViewById(R.id.package_title);
        edtPrice = (EditText) findViewById(R.id.package_price_id);
        service_list = (ListView) findViewById(R.id.service_list_in_package);

        //retrieve needed data
        packages = (Package) intent.getSerializableExtra("packageToModify");
        listService = (ArrayList<Service>) intent.getSerializableExtra("serviceForPackage");
        if((ArrayList<Service>) intent.getSerializableExtra("removedServiceForPackage") != null)
            removedServices = (ArrayList<Service>) intent.getSerializableExtra("removedServiceForPackage");
        namePackage = (String) intent.getStringExtra("packageName");
        pricePackage = (Integer) intent.getIntExtra("packagePrice", 0);

        //check for non null values
        if(namePackage != null)
            edtName.setText(namePackage);
        else
            edtName.setText(packages.getName());
        if(pricePackage != 0)
            edtPrice.setText(String.valueOf(pricePackage));
        else
            edtPrice.setText(String.valueOf(packages.getPrice()));



        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listService);
        registerForContextMenu(service_list);
        service_list.setAdapter(adapter);

        initFirebase();


    }

    //Firebase initialization
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Get firebase instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

    }

    public void savePackageUpdate(View view){
        namePackage = edtName.getText().toString();
        try{
            pricePackage = Integer.parseInt(edtPrice.getText().toString());
        } catch (NumberFormatException ex){
            int total = 0;
            for(Service s : listService)
                total =+ s.getPrice();
            pricePackage = total;
        }

        if(TextUtils.isEmpty(namePackage)) {
            edtName.setError("Cannot be empty");
            return;
        }

        if(pricePackage == 0){
            Toast.makeText(UpdatePackage.this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }

        //SavePackage
        removeDeletedServices();
        addJoinEntry();
        updatePackage();
        Intent intent = new Intent(UpdatePackage.this, ListPackages.class);
        startActivity(intent);
        this.finish();
    }

    private void removeDeletedServices(){

        if(removedServices.size()>0){
            for(Service s: removedServices){
                final String sId = s.getId();

                mDatabaseReference.child("packageServiceJoins").addValueEventListener(new ValueEventListener() {
                    @Override
                    //Go to join table and delete
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            final PackageServiceJoin packageServiceJoin = postSnapshot.getValue(PackageServiceJoin.class);
                            if(packageServiceJoin.serviceID.equals(sId)){
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
        }



    }

    private void addJoinEntry(){

        final ArrayList<String> oldList = new ArrayList<>();
        mDatabaseReference.child("packageServiceJoins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PackageServiceJoin join = postSnapshot.getValue(PackageServiceJoin.class);
                    if(packages.getId().equals(join.packageID))
                        oldList.add(join.serviceID);
                }

                List<Service> toRemove = new ArrayList<Service>();

                outerloop:
                for (Service s : listService) {
                    for(String id : oldList){
                        if(s.getId().equals(id)){
                            toRemove.add(s);
                            continue outerloop;
                        }
                    }

                    listService.removeAll(toRemove);
                    PackageServiceJoin packageServiceJoin = new PackageServiceJoin(UUID.randomUUID().toString(), packages.getId(), s.getId());
                    mDatabaseReference.child("packageServiceJoins").child(packageServiceJoin.getId()).setValue(packageServiceJoin);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updatePackage(){
        mDatabaseReference.child("packages").child(packages.getId()).child("name").setValue(namePackage);
        mDatabaseReference.child("packages").child(packages.getId()).child("price").setValue(pricePackage);
    }

    public void addServicesToPackage(View view) {

        namePackage = edtName.getText().toString();
        pricePackage = Integer.parseInt(edtPrice.getText().toString());

        Intent intent = new Intent(UpdatePackage.this, EditListServiceForPackage.class);
        intent.putExtra("serviceForPackage", (Serializable) listService);
        intent.putExtra("removedServiceForPackage", (Serializable) removedServices);
        intent.putExtra("packageToEdit", packages);
        intent.putExtra("packageName", namePackage);
        intent.putExtra("packagePrice", pricePackage);

        startActivity(intent);
        finish();


    }

    //Update and delete long click
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Select action :");
        menu.add(Menu.NONE, 0, Menu.NONE, "Delete");


    }

    //pressing on delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Service service = listService.get(info.position);

        switch (item.getItemId()) {

            case 0: //Delete
            {
                new AlertDialog.Builder(UpdatePackage.this)
                        .setMessage("Do you want to delete ? " + service.getName().toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removedServices.add(service);
                                listService.remove(service);
                                adapter.notifyDataSetChanged();
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

    public void cancelPackageUpdate (View view){
        Intent intent = new Intent(UpdatePackage.this, ListPackages.class);
        startActivity(intent);
        this.finish();
    }

    public void deletePackageButton(View view){
        new AlertDialog.Builder(UpdatePackage.this)
                .setMessage("Do you want to delete ? " + packages.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteServiceForPackage();
                        deletePackage();
                        Intent intent = new Intent(UpdatePackage.this, ListPackages.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void deleteServiceForPackage(){

        mDatabaseReference.child("packageServiceJoins").addValueEventListener(new ValueEventListener() {
            @Override
            //Go to join table and delete
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final PackageServiceJoin packageServiceJoin = postSnapshot.getValue(PackageServiceJoin.class);
                    if(packageServiceJoin.packageID.equals(packages.getId())){
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

    //Delete from DB
    private void deletePackage(){
        mDatabaseReference.child("packages").child(packages.getId()).removeValue();
    }

}
