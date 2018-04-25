package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

public class UpdateClient extends Activity {

    private EditText nameClient;
    private EditText phoneClient;
    private EditText addressClient;
    private EditText NPAClient;
    private EditText localityClient;
    private EditText countryClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_modify);
        Intent intent = getIntent();





    }
}
