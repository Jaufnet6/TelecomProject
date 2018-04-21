package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class ListClient extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        Intent intent = getIntent();

    }



    public void changeToCreateClient(View view){
        Intent intent = new Intent(ListClient.this, AddClient.class);
        startActivity(intent);

    }
}
