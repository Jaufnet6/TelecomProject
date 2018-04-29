/*
Author : Daniela Lourenço and Jaufray Sornette
Date : mars - avril 2018
 */
package com.example.jaufray.telecomproject;



import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView txtDevelopped ;

     private DrawerLayout mDrawerLayout;
    //Class to tie the functionnality of DraweLayout and the framework ActionBar
    //to implement the recommended design for navigation drawers
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Affiche le layout "About" où l'on trouve déjà nos informations
        setContentView(R.layout.about);
        Intent intent = getIntent();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_about);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_about);
        navigationView.setNavigationItemSelectedListener(this);



    }


    //Drawer top button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
           return true;
        }
        return super.onOptionsItemSelected(item);

    }

    //Drawer button
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(About.this, Languages.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_about:
                android.widget.Toast.makeText(this, getString(R.string.al_main_about), Toast.LENGTH_LONG).show();
                finish();
                return true;

            case R.id.nav_client:
                Intent intent3 = new Intent(About.this, ListClient.class);
                startActivity(intent3);
                finish();
                return true;

            case R.id.nav_main_menu:
                Intent intent2 = new Intent(About.this, MainMenu.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(About.this, ListPackages.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(About.this, ListServices.class);
                startActivity(intent5);
                finish();
                return true;

            default:
                return false;

        }
    }





}
