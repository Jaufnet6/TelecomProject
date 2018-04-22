package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class UpdateService extends Activity{

    private EditText edtName;
    private EditText edtDescription;
    private EditText edtPrice;

    private String nameEdt;
    private String descriptionEdt;
    private Integer priceEdt;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_edit);

        Intent intent = getIntent();
        Service service = (Service) intent.getParcelableExtra("serviceToModify");


        edtName = (EditText) findViewById(R.id.edt_name_service);
        edtDescription = (EditText) findViewById(R.id.edt_description_name);
        edtPrice = (EditText) findViewById(R.id.edit_price);
    }
}
