package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Client;
import com.example.jaufray.telecomproject.Model.Package;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditListPackageForClient extends AppCompatActivity {

    private ListView list_package;
    //Database
    private CompositeDisposable compositeDisposable;
    private PackageRepository packageRepository;

    //Adapter
    private List<Package> packageList = new ArrayList<Package>();
    private ArrayAdapter adapter;

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private String clientNPA;
    private String clientLocality;
    private String clientCountry;

    private Client client;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_choice_for_client);
        final Intent intent = getIntent();
        //intent.putExtra("PackageForClient", (Serializable) packages);

        compositeDisposable = new CompositeDisposable();

        //Init View
        list_package = (ListView) findViewById(R.id.package_list_client);

        //Retrieve user inputs from creation frame
        clientName = (String) intent.getStringExtra("clientName");
        clientPhone = (String) intent.getStringExtra("clientPhone");
        clientAddress = (String) intent.getStringExtra("clientAddress");
        clientNPA = (String) intent.getStringExtra("clientNPA");
        clientLocality = (String) intent.getStringExtra("clientLocality");
        clientCountry = (String) intent.getStringExtra("clientCountry");
        client = (Client) intent.getSerializableExtra("clientToModify");



        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, packageList);
        registerForContextMenu(list_package);
        list_package.setAdapter(adapter);

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));

        //Load all data from DB
        loadData();

        list_package.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Package packages = (Package) adapterView.getAdapter().getItem(i);
                Intent intent1 = new Intent(EditListPackageForClient.this, UpdateClient.class);
                intent1.putExtra("packageToClient", packages);
                intent1.putExtra("clientName", clientName);
                intent1.putExtra("clientPhone", clientPhone);
                intent1.putExtra("clientAddress", clientAddress);
                intent1.putExtra("clientNPA", clientNPA);
                intent1.putExtra("clientCountry", clientCountry);
                intent1.putExtra("clientLocality", clientLocality);
                intent1.putExtra("clientToModify", client);
                startActivity(intent1);
                finish();
            }
        });
    }




    private void loadData() {

        //Use RxJava
        Disposable disposable = packageRepository.getAllPackages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Package>>() {
                               @Override
                               public void accept(List<Package> packages) throws Exception{
                                   onGetAllPackageSuccess(packages);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(EditListPackageForClient.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetAllPackageSuccess(List<Package> packages) {
        packageList.clear();
        packageList.addAll(packages);
        adapter.notifyDataSetChanged();
    }


}