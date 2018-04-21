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


public class MainMenu extends AppCompatActivity {

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
            switch (item.getItemId()) {
                case R.id.nav_language:
                    Intent intent1 = new Intent(MainMenu.this, Languages.class);
                    startActivity(intent1);
                    return true;

                case R.id.nav_about:
                    Intent intent2 = new Intent(MainMenu.this, About.class);
                    startActivity(intent2);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);

            }
        }

        return super.onOptionsItemSelected(item);

    }


}
