package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListPackages extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        Intent intent = getIntent();

    }

    public void changeToCreatePackage(View view){
        Intent intent = new Intent(ListPackages.this, AddPackage.class);
        startActivity(intent);

    }


}
