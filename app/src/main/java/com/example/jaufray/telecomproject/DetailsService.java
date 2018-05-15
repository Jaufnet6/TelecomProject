package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class DetailsService extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView serviceName;
    private TextView serviceDescription;
    private TextView servicePrice;

    private Service service;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


     private DrawerLayout mDrawerLayout;
    //Class to tie the functionnality of DraweLayout and the framework ActionBar
    //to implement the recommended design for navigation drawers
    private ActionBarDrawerToggle mToggle;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_description);

        Intent intent = getIntent();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_details_service);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_details_service);
        navigationView.setNavigationItemSelectedListener(this);

        service = (Service) intent.getSerializableExtra("DetailsService");

        serviceName = (TextView) findViewById(R.id.id_service_name);
        serviceDescription = (TextView) findViewById(R.id.id_service_details);
        servicePrice = (TextView) findViewById(R.id.id_service_price);

        serviceName.setText(service.getName());
        serviceDescription.setText(service.getDescription());
        servicePrice.setText(String.valueOf(service.getPrice()));


    }

    //Drawer top button
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
           return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(DetailsService.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(DetailsService.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Intent intent6 = new Intent(DetailsService.this, ListClient.class);
                startActivity(intent6);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent3 = new Intent(DetailsService.this, MainMenu.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(DetailsService.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(DetailsService.this, ListServices.class);
                startActivity(intent5);
                finish();
                return true;

            default:
                return false;

        }
    }

    //Delete service button
    public void deleteServiceDetails(View view){

        new AlertDialog.Builder(DetailsService.this)
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
    //go to edit view
    public void editServiceDetails(View view){

        Intent intent = new Intent(DetailsService.this, UpdateService.class);
        intent.putExtra("serviceToModify", service);
        startActivity(intent);
        finish();

    }
    //delete service
    private void deleteService(final Service service) {

      mDatabaseReference.child("service").child(service.getId()).removeValue();
      Intent intent = new Intent(DetailsService.this, ListServices.class);
      startActivity(intent);
      finish();

    }


}
