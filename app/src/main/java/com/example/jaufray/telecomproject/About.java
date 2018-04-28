/*
Author : Daniela Lourenço and Jaufray Sornette
Date : mars - avril 2018
 */
package com.example.jaufray.telecomproject;



import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class About extends AppCompatActivity{

    private TextView txtDevelopped ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Affiche le layout "About" où l'on trouve déjà nos informations
        setContentView(R.layout.about);
        Intent intent = getIntent();



    }





}
