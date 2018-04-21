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

    /*
    private void addListenerOnButton_delete() {
        ImageButton imageB = (ImageButton) findViewById(R.id.delete_button);
        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListClient.this, DetailsClient.class);
                startActivity(intent);
            }
        });

    }

    */
/*
    private void addListenerOnButton() {


        imageButton = (ImageButton) findViewById(R.id.add_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListClient.this, AddClient.class);


                startActivity(intent);
            }
        });


    }
    */
}
