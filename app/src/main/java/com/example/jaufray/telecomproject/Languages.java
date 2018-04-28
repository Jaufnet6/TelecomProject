/*
Author : Daniela Lourenço and Jaufray Sornette
Date : mars - avril 2018
 */
package com.example.jaufray.telecomproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.preference.ListPreference;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;


public class Languages extends AppCompatActivity implements View.OnClickListener {

    //Creation of buttons to make the link with the buttons of the layout "Languages"
    private ImageButton francais;
    private ImageButton english;


    private Locale myLocale;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);
        Intent intent = getIntent();

        //Lié bouton
        francais = findViewById(R.id.language_french_button);
        francais.setOnClickListener(this);

        english = findViewById(R.id.language_english_button);
        english.setOnClickListener(this);

        loadLocale();
    }

    //Loading a saved locale
    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    //Save the current locale
    private void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    //Changing the language in the application
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        //If lang == fr, my locale is french and not the language by default.
        myLocale = new Locale(lang);
        saveLocale(lang);
        //This method sets the default locale
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }



    @Override
    public void onClick(View v) {

        //If we choose, for example, "en" in lang, we call the method "ChangeLang"
        String lang ="en";
        switch (v.getId())
        {
            case R.id.language_english_button:
                lang="en";
                break;

            case R.id.language_french_button:
                lang="fr";
                break;
            default:
                break;
        }
        changeLang(lang);
        //We close the Languages layout an we redirect the user to the main menu
        Intent intent = new Intent(Languages.this, MainMenu.class);
        startActivity(intent);
        finish();

    }

    @Override
    //Methode to change the configuaration of our application. Updating the resources used in our interface
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

}