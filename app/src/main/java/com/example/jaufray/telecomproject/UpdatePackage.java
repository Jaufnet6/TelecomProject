package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.jaufray.telecomproject.Model.Service;

public class UpdatePackage extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);

        Intent intent = getIntent();
        //service = (Service) intent.getSerializableExtra("serviceToModify");


    }
}
