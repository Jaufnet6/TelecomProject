package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.ClientRepository;
import com.example.jaufray.telecomproject.Local.ClientDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Service;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ListClient extends Activity {

    private ListView list_client;

    //Database
    private CompositeDisposable compositeDisposable;
    private ClientRepository clientRepository;

    //Adapter
    List<Client> clientList = new ArrayList<>();
    ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        Intent intent = getIntent();


        // Init
        compositeDisposable = new CompositeDisposable();

        // Init View
        list_client = (ListView) findViewById(R.id.full_client_liste);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, clientList);
        registerForContextMenu(list_client);
        list_client.setAdapter(adapter);

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        clientRepository = ClientRepository.getInstance(ClientDataSource.getInstance(telecomDatabase.clientDAO()));

        //Load all data from DB
        loadData();

        list_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Client client = (Client) adapterView.getAdapter().getItem(i);

                Intent intent1 = new Intent(ListClient.this, DetailsClient.class);
                intent1.putExtra("DetailsClient", client);
                startActivity(intent1);
            }
        });


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
        this.finish();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action :");

        menu.add(Menu.NONE,0, Menu.NONE, "Update");
        menu.add(Menu.NONE,1, Menu.NONE, "Delete");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Client clients = clientList.get(info.position);

        switch (item.getItemId())
        {
            case 0: //Update
            {
                /*
                Intent intent = new Intent(ListPackages.this, UpdateService.class);
                intent.putExtra("serviceToModify", packages);
                startActivity(intent);
                */

            }
            break;
            case 1: //Delete
            {
                new AlertDialog.Builder(ListClient.this)
                        .setMessage("Do you want to delete ? " + clients.getName().toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteClient(clients);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
            break;
        }
        return true;
    }

    private void deleteClient(final Client clients) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {


            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                clientRepository.deleteClient(clients);
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
                                Toast.makeText(ListClient.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        }

                );

        compositeDisposable.add(disposable);

    }


}
