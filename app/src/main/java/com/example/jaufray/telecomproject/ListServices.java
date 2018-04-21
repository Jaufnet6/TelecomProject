package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListServices extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        Intent intent = getIntent();

    }

    public void changeToCreateService(View view){
        Intent intent = new Intent(ListServices.this, AddService.class);
        startActivity(intent);

    }



}
