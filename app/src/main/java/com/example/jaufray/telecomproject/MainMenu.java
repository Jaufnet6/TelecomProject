package com.example.jaufray.telecomproject;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_main);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public void changeToClient(View view){

        Intent intent = new Intent(MainMenu.this, ListClient.class);
        startActivity(intent);

    }

    public void changeToService(View view){
        Intent intent = new Intent(MainMenu.this, ListServices.class);
        startActivity(intent);
    }

    public void changeToPackage(View view){
        Intent intent = new Intent(MainMenu.this, ListPackages.class);
        startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
           return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_language:
                Intent intent1 = new Intent(MainMenu.this, Languages.class);
                startActivity(intent1);
                return true;

            case R.id.nav_about:
                Intent intent2 = new Intent(MainMenu.this, About.class);
                startActivity(intent2);
                return true;

            case R.id.nav_client:
                Intent intent3 = new Intent(MainMenu.this, ListClient.class);
                startActivity(intent3);
                return true;

            case R.id.nav_main_menu:
                Toast.makeText(this, "You already are on the main Menu", Toast.LENGTH_LONG).show();
                return true;

            case R.id.nav_package:
                Intent intent4 = new Intent(MainMenu.this, ListPackages.class);
                startActivity(intent4);
                return true;

            case R.id.nav_service:
                Intent intent5 = new Intent(MainMenu.this, ListServices.class);
                startActivity(intent5);
                return true;

            default:
                return false;

        }
    }
}
