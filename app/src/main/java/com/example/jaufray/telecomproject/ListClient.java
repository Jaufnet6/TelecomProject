package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ClientRepository;
import com.example.jaufray.telecomproject.Local.ClientDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Client;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ListClient extends Activity {

    private ListView list_client;

    //Database
    private CompositeDisposable compositeDisposable;
    private ClientRepository clientRepository;

    //Adapter
    List<Client> clientList;
    ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        Intent intent = getIntent();


        // Init
        compositeDisposable = new CompositeDisposable();

        // Init View
        list_client = (ListView) findViewById(R.id.list_client);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, clientList);
        registerForContextMenu(list_client);
        list_client.setAdapter(adapter);

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        clientRepository = ClientRepository.getInstance(ClientDataSource.getInstance(telecomDatabase.clientDAO()));

        //Load all data from DB
        loadData();

        //Event


    }

    private void loadData() {

        //Use RxJava
        Disposable disposable = clientRepository.getAllClients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Client>>() {
                               @Override
                               public void accept(List<Client> clients) throws Exception {
                                   onGetAllClientSuccess(clients);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(ListClient.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllClientSuccess(List<Client> clients) {
        clientList.clear();
        clientList.addAll(clients);
        adapter.notifyDataSetChanged();


    }


    public void changeToCreateClient(View view) {
        Intent intent = new Intent(ListClient.this, AddClient.class);
        startActivity(intent);

    }
}
