package com.example.jaufray.telecomproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.preference.ListPreference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;


public class Languages extends Activity implements View.OnClickListener {
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
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }







    @Override
    public void onClick(View v) {

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
        Intent intent = new Intent(Languages.this, MainMenu.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

}