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
import android.view.Menu;
import android.view.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ListClient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView list_client;
    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    //Database
    private CompositeDisposable compositeDisposable;

    //Adapter
    List<Client> clientList = new ArrayList<>();
    ArrayAdapter adapter;

    //Drawer Menu
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        Intent intent = getIntent();

        //Menu Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.client_list_activity);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_client);
        navigationView.setNavigationItemSelectedListener(this);


//Firebase
        initFirebase();
        addFirebaseListener();


        // Init View
        list_client = (ListView) findViewById(R.id.full_client_liste);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, clientList);
        registerForContextMenu(list_client);
        list_client.setAdapter(adapter);



        list_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Client client = (Client) adapterView.getAdapter().getItem(i);

                Intent intent1 = new Intent(ListClient.this, DetailsClient.class);
                intent1.putExtra("DetailsClient", client);
                finish();
                startActivity(intent1);
            }
        });


    }


    private void addFirebaseListener() {
        mDatabaseReference.child("clients").addValueEventListener(new ValueEventListener() {
            @Override
            //Retrieve data from firebase
            //DataSnapShot : contient les données provenant d'un emplacement de bd Firebase - on recoit les données en tant que DataSnapShot
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (clientList.size() > 0) {
                    clientList.clear();
                }

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Client client = postSnapshot.getValue(Client.class);
                    clientList.add(client);
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

    //Menu Drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //Drawer buttons
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(ListClient.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(ListClient.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Toast.makeText(this, getString(R.string.al_main_client), Toast.LENGTH_LONG).show();
                return true;

            case R.id.nav_main_menu:
                Intent intent3 = new Intent(ListClient.this, MainMenu.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(ListClient.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(ListClient.this, ListServices.class);
                startActivity(intent5);
                finish();
                return true;

            default:
                return false;

        }
    }


    //put client in arraylist
    private void onGetAllClientSuccess(List<Client> clients) {
        clientList.clear();
        clientList.addAll(clients);
        adapter.notifyDataSetChanged();

    }

    //create new client
    public void changeToCreateClient(View view) {
        Intent intent = new Intent(ListClient.this, AddClient.class);
        startActivity(intent);
        this.finish();

    }

    //long click on one client (delete quick)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action :");

        menu.add(Menu.NONE,0, Menu.NONE, "Delete");

    }

    //Delete quick button
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Client clients = clientList.get(info.position);

        switch (item.getItemId())
        {
            case 0: //Delete
            {
                new AlertDialog.Builder(ListClient.this)
                        .setMessage("Do you want to delete ? " + clients.getName().toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteClient(clients);
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
    //Delete client
    private void deleteClient(final Client clients) {
        mDatabaseReference.child("clients").child(clients.getId()).removeValue();
        adapter.notifyDataSetChanged();

    }


}
