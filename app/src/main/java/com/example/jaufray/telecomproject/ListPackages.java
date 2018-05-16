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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ListPackages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ListView list_packages;

    //Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    //Adapter
    List<Package> packageList = new ArrayList<>();
    ArrayAdapter adapter;

    private ArrayList<Service> listService = new ArrayList<Service>();

    //Drawer Menu
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        Intent intent = getIntent();

        //Drawer Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_package);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_package);
        navigationView.setNavigationItemSelectedListener(this);


        // Init View
        list_packages = (ListView) findViewById(R.id.full_package_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, packageList);
        registerForContextMenu(list_packages);
        list_packages.setAdapter(adapter);

        //Firebase
        initFirebase();
        addFirebaseListener();


        //Click on one package
        list_packages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Package packages = (Package) adapterView.getAdapter().getItem(i);

                Intent intent1 = new Intent(ListPackages.this, DetailsPackage.class);
                intent1.putExtra("DetailPackages", packages);
                startActivity(intent1);
                finish();
            }
        });

    }
    //button top drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

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
        //Get Firebase instance
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    //Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(ListPackages.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(ListPackages.this, About.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_client:
                Intent intent3 = new Intent(ListPackages.this, ListClient.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent4 = new Intent(ListPackages.this, MainMenu.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_package:
                Toast.makeText(this,getString(R.string.al_main_package), Toast.LENGTH_LONG).show();
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(ListPackages.this, ListServices.class);
                startActivity(intent5);
                finish();
                return true;

            default:
                return false;

        }
    }

    //create new package
    public void changeToCreatePackage(View view){
        Intent intent = new Intent(ListPackages.this, AddPackage.class);
        startActivity(intent);
        finish();
    }

}
