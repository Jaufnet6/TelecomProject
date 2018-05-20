package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import java.io.Serializable;
import java.util.ArrayList;

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
    private Integer pricePackage;

    private Package packages;

    private ArrayAdapter adapter;



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


        this.finish();
    }

    //Create a row in the table between package and service for the many to many relation
    public void addDataLinkService(){

        String id = packages.getId();


        for(Service s : listService){


        }
    }

    public void addServicesToPackage(View view) {

        namePackage = edtName.getText().toString();
        pricePackage = Integer.parseInt(edtPrice.getText().toString());

        Intent intent = new Intent(UpdatePackage.this, EditListServiceForPackage.class);
        intent.putExtra("serviceForPackage", (Serializable) listService);
        intent.putExtra("packageToEdit", packages);
        intent.putExtra("packageName", namePackage);
        intent.putExtra("packagePrice", pricePackage);

        startActivity(intent);
        finish();


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
                        deletePackage();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    //delete package
    private void deletePackage(){



    }

    //Method to delete all rows in the middle table between package and service where the idPackage is present
    private void deleteLinkPackageToService() {

        for(Service s : listService){



        }


    }
}
