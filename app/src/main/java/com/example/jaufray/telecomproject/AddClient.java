package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;

import org.w3c.dom.Text;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class AddClient extends AppCompatActivity {


    private EditText nameClient;
    private EditText phoneClient;
    private EditText addressClient;
    private EditText NPAClient;
    private EditText localityClient;
    private EditText countryClient;


    private Package pack = new Package();
    private String idPackage;

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private String clientNPA;
    private String clientLocality;
    private String clientCountry;


    private TextView packTxt;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);
        Intent intent = getIntent();

        nameClient = (EditText) findViewById(R.id.et_name);
        phoneClient = (EditText) findViewById(R.id.et_phoneNumber);
        addressClient = (EditText) findViewById(R.id.et_street);
        NPAClient = (EditText) findViewById(R.id.et_npa);
        localityClient = (EditText) findViewById(R.id.et_city);
        countryClient = (EditText) findViewById(R.id.et_country);

        packTxt = (TextView) findViewById(R.id.name_package_client);
        //Retrieve user inputs from creation frame
        clientName = (String) intent.getStringExtra("clientName");
        clientPhone = (String) intent.getStringExtra("clientPhone");
        clientAddress = (String) intent.getStringExtra("clientAddress");
        clientNPA = (String) intent.getStringExtra("clientNPA");
        clientLocality = (String) intent.getStringExtra("clientLocality");
        clientCountry = (String) intent.getStringExtra("clientCountry");

        if(clientName != null)
            nameClient.setText(clientName);
        if(clientPhone != null)
            phoneClient.setText(clientPhone);
        if(clientAddress != null)
            addressClient.setText(clientAddress);
        if(clientNPA != null)
            NPAClient.setText(clientNPA);
        if(clientLocality != null)
            localityClient.setText(clientLocality);
        if(clientCountry != null)
            countryClient.setText(clientCountry);

        //Ajouter le package
        //Retrieve package from list of package choice
        pack = (Package) intent.getSerializableExtra("PackageForClient");

        if(pack != null){
            packTxt.setText(pack.getName());
            idPackage = pack.getId();

        }


    }

    //go to list of packages to choose 1
    public void openListPackages(View v) {
        getUserInput();
        Intent intent = new Intent(AddClient.this, ListPackageForClient.class);
        intent.putExtra("clientName", clientName);
        intent.putExtra("clientPhone", clientPhone);
        intent.putExtra("clientAddress", clientAddress);
        intent.putExtra("clientNPA", clientNPA);
        intent.putExtra("clientCountry", clientCountry);
        intent.putExtra("clientLocality", clientLocality);
        startActivity(intent);

        finish();
    }


    public void getUserInput(){

        clientName = nameClient.getText().toString();
        clientPhone = phoneClient.getText().toString();
        clientAddress = addressClient.getText().toString();
        clientNPA = NPAClient.getText().toString();
        clientCountry = countryClient.getText().toString();
        clientLocality = localityClient.getText().toString();

    }


    //Save client button
    public void saveClient(View view) {

        //Database

        getUserInput();

        if (TextUtils.isEmpty(clientName)) {
            nameClient.setError("Cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(clientPhone)) {
            phoneClient.setError("Cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(clientAddress)) {
            addressClient.setError("Cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(clientNPA)) {
            NPAClient.setError("Cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(clientLocality)) {
            localityClient.setError("Cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(clientCountry)) {
            countryClient.setError("Cannot be empty");
            return;
        }


    }
    //Cancel client creation
    public void cancelClientAdd(View view) {

        Intent intent = new Intent(AddClient.this, ListClient.class);
        startActivity(intent);
        this.finish();
    }


}
