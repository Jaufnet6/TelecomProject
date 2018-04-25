package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ClientRepository;
import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Local.ClientDataSource;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;

import java.io.Serializable;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class DetailsClient extends Activity {

    private TextView nameClient;
    private TextView phoneClient;
    private TextView addressClient;
    private TextView NPAClient;
    private TextView localityClient;
    private TextView countryClient;

    private TextView namePackage;
    private TextView pricePackage;


    int idPack;

    private Client client;
    private Package pack;

    private ClientRepository clientRepository;
    private PackageRepository packageRepository;
    private CompositeDisposable compositeDisposable;


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
        namePackage = (TextView)findViewById(R.id.dt_name_package) ;


        nameClient.setText(client.getName());
        phoneClient.setText(client.getPhone());
        addressClient.setText(client.getAddress());
        NPAClient.setText(client.getNpa());
        localityClient.setText(client.getLocality());
        countryClient.setText(client.getCountry());
        idPack = client.getIdPackage();
        namePackage.setText(pack.getName());

        // Init
        compositeDisposable = new CompositeDisposable();

        //Database
        //Create daze base
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this);
        clientRepository = ClientRepository.getInstance(ClientDataSource.getInstance(telecomDatabase.clientDAO()));
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));



        loadData();

    }


    private void loadData() {
        //Use RxJava
        Disposable disposable = clientRepository.getClientById(client.getId())
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
                                Toast.makeText(DetailsClient.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetPackageSucess(Package pack) {
    }

    public void editClient (View view){

        Intent intent = new Intent(DetailsClient.this, UpdateClient.class);
        intent.putExtra("clientToModify", client);
        intent.putExtra("packageToClient", (Serializable) pack);
        startActivity(intent);
        this.finish();

    }

    public void deleteClient(){

        new AlertDialog.Builder(DetailsClient.this)
                .setMessage("Do you want to delete ? " + client.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteClient();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();


    }


}