package com.example.jaufray.telecomproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.preference.ListPreference;
import android.view.View;
import android.widget.Button;

import java.util.Locale;


public class Languages extends Activity implements View.OnClickListener{
    private Button francais;
    private Button english;
    private Locale myLocale;

    private final String TAG = "LanguageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);
        Intent intent = getIntent();

    }



    @Override
    public void onClick(View v) {

    }
}