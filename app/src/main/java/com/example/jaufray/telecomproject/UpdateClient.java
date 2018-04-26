package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ClientRepository;
import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Local.ClientDataSource;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpdateClient extends Activity {

    private EditText nameClient;
    private EditText phoneClient;
    private EditText addressClient;
    private EditText NPAClient;
    private EditText localityClient;
    private EditText countryClient;

    private TextView namePackage;
    private TextView pricePackage;

    private ClientRepository clientRepository;
    private CompositeDisposable compositeDisposable;

    private Package packageClient;
    private Client client;

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private String clientNPA;
    private String clientLocality;
    private String clientCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_modify);
        Intent intent = getIntent();

        packageClient = (Package) intent.getSerializableExtra("packageToClient");
        client = (Client) intent.getSerializableExtra("clientToModify");
        clientName = (String) intent.getStringExtra("clientName");
        clientPhone = (String) intent.getStringExtra("clientPhone");
        clientAddress = (String) intent.getStringExtra("clientAddress");
        clientNPA = (String) intent.getStringExtra("clientNPA");
        clientLocality = (String) intent.getStringExtra("clientLocality");
        clientCountry = (String) intent.getStringExtra("clientCountry");

        if(clientName == null)
            clientName = client.getName();
        if(clientPhone == null)
            clientPhone = client.getPhone();
        if(clientAddress == null)
            clientAddress = client.getAddress();
        if(clientNPA == null)
            clientNPA = client.getNpa();
        if(clientLocality == null)
            clientLocality = client.getLocality();
        if(clientCountry == null)
            clientCountry = client.getCountry();


        //Connect to Activity
        nameClient = (EditText)findViewById(R.id.et_name);
        phoneClient = (EditText)findViewById(R.id.et_phoneNumber);
        addressClient = (EditText)findViewById(R.id.et_street);
        NPAClient = (EditText)findViewById(R.id.et_npa);
        localityClient = (EditText)findViewById(R.id.et_city);
        countryClient = (EditText)findViewById(R.id.et_country);
        pricePackage = (TextView)findViewById(R.id.dt_price_package);
        namePackage = (TextView)findViewById(R.id.dt_name_package) ;

        nameClient.setText(clientName);
        phoneClient.setText(clientPhone);
        addressClient.setText(clientAddress);
        NPAClient.setText(clientNPA);
        localityClient.setText(clientLocality);
        countryClient.setText(clientCountry);
        pricePackage.setText(packageClient.getPrice() + " CHF");
        namePackage.setText(packageClient.getName());



        //Database
        //Instantiate connection to database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this);
        clientRepository = ClientRepository.getInstance(ClientDataSource.getInstance(telecomDatabase.clientDAO()));

        // Init
        compositeDisposable = new CompositeDisposable();



    }

    public void modifyClientPackage(View view){

        getUserInput();
        Intent intent = new Intent(UpdateClient.this, EditListPackageForClient.class);
        intent.putExtra("clientName", clientName);
        intent.putExtra("clientPhone", clientPhone);
        intent.putExtra("clientAddress", clientAddress);
        intent.putExtra("clientNPA", clientNPA);
        intent.putExtra("clientCountry", clientCountry);
        intent.putExtra("clientLocality", clientLocality);
        startActivity(intent);

        this.finish();;


    }

    public void getUserInput(){

        nameClient = (EditText) findViewById(R.id.et_name);
        phoneClient = (EditText) findViewById(R.id.et_phoneNumber);
        addressClient = (EditText) findViewById(R.id.et_street);
        NPAClient = (EditText) findViewById(R.id.et_npa);
        localityClient = (EditText) findViewById(R.id.et_city);
        countryClient = (EditText) findViewById(R.id.et_country);

        clientName = nameClient.getText().toString();
        clientPhone = phoneClient.getText().toString();
        clientAddress = addressClient.getText().toString();
        clientNPA = NPAClient.getText().toString();
        clientCountry = countryClient.getText().toString();
        clientLocality = localityClient.getText().toString();

    }

    public void saveClientModified(View view) {

        getUserInput();
        try {
            Thread.sleep(30);
        }catch (Exception ex){}

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

        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                getUserInput();
                client.setName(clientName);
                client.setPhone(clientPhone);
                client.setAddress(clientAddress);
                client.setNpa(clientNPA);
                client.setLocality(clientLocality);
                client.setCountry(clientCountry);
                client.setIdPackage(packageClient.getId());

                clientRepository.updateClient(client);

                e.onComplete();
            }

        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(UpdateClient.this, "Client added!", Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(UpdateClient.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ,

                        new Action() {

                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(UpdateClient.this, ListClient.class);
                                startActivity(intent);

                                finish();;
                            }
                        }
                );
    }


    public void cancelClientModified (View view){
        Intent intent = new Intent(UpdateClient.this, ListClient.class);
        startActivity(intent);
        this.finish();;

    }

    public void deleteClientModified(View view){

        new AlertDialog.Builder(UpdateClient.this)
                .setMessage("Do you want to delete ? " + client.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteClientDB(client);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();


    }

    private void deleteClientDB(final Client client) {

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {


            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                clientRepository.deleteClient(client);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           },

                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(UpdateClient.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(UpdateClient.this, ListClient.class);
                                startActivity(intent);
                            }
                        }

                );

        compositeDisposable.add(disposable);
        this.finish();;


    }




}
