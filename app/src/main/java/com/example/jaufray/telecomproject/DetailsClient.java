package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;


public class DetailsClient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView nameClient;
    private TextView phoneClient;
    private TextView addressClient;
    private TextView NPAClient;
    private TextView localityClient;
    private TextView countryClient;

    private TextView namePackage;
    private TextView pricePackage;

    private Client client;
    private Package clientpack;

    private DrawerLayout mDrawerLayout;
    //Class to tie the functionnality of DraweLayout and the framework ActionBar
    //to implement the recommended design for navigation drawers
    private ActionBarDrawerToggle mToggle;
    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        Intent intent = getIntent();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_clientdetails);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_client);
        navigationView.setNavigationItemSelectedListener(this);

        initFirebase();


        //get the client that was given when clicked in the list
        client = (Client) intent.getSerializableExtra("DetailsClient");

        getPackageOfClient(client.getIdPackage());



        //Link to TextViews in the activity
        nameClient = (TextView)findViewById(R.id.dt_name);
        phoneClient = (TextView)findViewById(R.id.dt_phoneNumber);
        addressClient = (TextView)findViewById(R.id.dt_street);
        NPAClient = (TextView)findViewById(R.id.dt_postal_code);
        localityClient = (TextView)findViewById(R.id.dt_city);
        countryClient = (TextView)findViewById(R.id.dt_country);
        pricePackage = (TextView)findViewById(R.id.dt_price_package);
        namePackage = (TextView)findViewById(R.id.dt_name_package) ;

        //Fill the textview fields
        nameClient.setText(client.getName());
        phoneClient.setText(client.getPhone());
        addressClient.setText(client.getAddress());
        NPAClient.setText(client.getNpa());
        localityClient.setText(client.getLocality());
        countryClient.setText(client.getCountry());



    }
    //Firebase initialization
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Get firebase instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
           return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void getPackageOfClient(String id) {

        final Package aPackage;

        mDatabaseReference.child("packages").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clientpack = dataSnapshot.getValue(Package.class);
                pricePackage.setText(Integer.toString(clientpack.getPrice()));
                namePackage.setText(clientpack.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void assignPackage(Package thePackage){
        clientpack = thePackage;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(DetailsClient.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(DetailsClient.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Intent intent6 = new Intent(DetailsClient.this, ListClient.class);
                startActivity(intent6);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent3 = new Intent(DetailsClient.this, MainMenu.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(DetailsClient.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(DetailsClient.this, ListServices.class);
                startActivity(intent5);
                finish();
                return true;

            default:
                return false;

        }
    }


    //put package in package and to view for the user
    public void onGetPackageSuccess(Package pack) {
        clientpack = pack;
        namePackage.setText(clientpack.getName());
        pricePackage.setText(clientpack.getPrice() + " CHF");
    }
    //Go to edit client
    public void editClient (View view){

        Intent intent = new Intent(DetailsClient.this, UpdateClient.class);
        intent.putExtra("clientToModify", client);
        intent.putExtra("packageToClient", clientpack);
        finish();
        startActivity(intent);
        this.finish();

    }
    //delete Client button
    public void deleteClient(View view){

        new AlertDialog.Builder(DetailsClient.this)
                .setMessage("Do you want to delete ? " + client.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteClientDB(client);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();


    }
    //Delete client in DB
    private void deleteClientDB(final Client client) {
        mDatabaseReference.child("clients").child(client.getId()).removeValue();
        Intent intent = new Intent(DetailsClient.this, ListClient.class);
        startActivity(intent);
        finish();


    }


}