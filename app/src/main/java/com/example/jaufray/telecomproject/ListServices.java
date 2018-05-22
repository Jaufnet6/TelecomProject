package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListServices extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ListView list_services;


    Activity activity;
    LayoutInflater inflater;
    //Adapter
    List<Service> serviceList = new ArrayList<>();
    ArrayAdapter adapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        final Intent intent = getIntent();

        //Drawer menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_services);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_services);
        navigationView.setNavigationItemSelectedListener(this);

        // Init View
        list_services = (ListView) findViewById(R.id.full_service_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        registerForContextMenu(list_services);
        list_services.setAdapter(adapter);


        //Firebase
        initFirebase();
        addFirebaseListener();

        list_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Service service = (Service) adapterView.getAdapter().getItem(i);

                Intent intent1 = new Intent(ListServices.this, DetailsService.class);
                intent1.putExtra("DetailsService", service);
                startActivity(intent1);
                finish();
            }
        });
    }

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

    //Drawer Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //Drawer Menu Buttons
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(ListServices.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(ListServices.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Intent intent3 = new Intent(ListServices.this, ListClient.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent5 = new Intent(ListServices.this, MainMenu.class);
                startActivity(intent5);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(ListServices.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Toast.makeText(this, getString(R.string.al_main_service), Toast.LENGTH_LONG).show();
                return true;

            default:
                return false;

        }
    }

    //Add a service button
    public void changeToCreateService(View view) {
        Intent intent = new Intent(ListServices.this, AddService.class);
        startActivity(intent);
        finish();
    }

    //Update and delete long click
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Select action :");

        menu.add(Menu.NONE, 0, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete");


    }

    //pressing on delete or update
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Service service = serviceList.get(info.position);

        switch (item.getItemId()) {
            case 0: //Update
            {
                Intent intent = new Intent(ListServices.this, UpdateService.class);
                intent.putExtra("serviceToModify", service);
                startActivity(intent);
                finish();

            }
            break;
            case 1: //Delete
            {
                new AlertDialog.Builder(ListServices.this)
                        .setMessage("Do you want to delete ? " + service.getName().toString())
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
            break;
        }
        return true;
    }


    //delete service
    private void deleteService(Service service) {
        mDatabaseReference.child("services").child(service.getId()).removeValue();
        adapter.notifyDataSetChanged();
    }


}
