package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jaufray.telecomproject.Model.Client;


public class DetailsClient extends Activity {

    private TextView nameClient;
    private TextView phoneClient;
    private TextView addressClient;
    private TextView NPAClient;
    private TextView localityClient;
    private TextView countryClient;

    private TextView namePackage;
    private TextView pricePackage;

    private Client client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        Intent intent = getIntent();

        nameClient = (TextView)findViewById(R.id.dt_name);
        phoneClient = (TextView)findViewById(R.id.dt_phoneNumber);
        addressClient = (TextView)findViewById(R.id.dt_street);
        NPAClient = (TextView)findViewById(R.id.dt_postal_code);
        localityClient = (TextView)findViewById(R.id.dt_city);
        countryClient = (TextView)findViewById(R.id.dt_country);
        namePackage = (TextView)findViewById(R.id.dt_name_package);
        pricePackage = (TextView)findViewById(R.id.dt_price_package);






    }

}