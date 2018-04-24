package com.example.jaufray.telecomproject;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.*;

import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;

import com.example.jaufray.telecomproject.Model.Package;

import java.io.Serializable;
import java.util.*;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.*;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by danie on 24.04.2018.
 */

public class ListPackageForClient extends Activity{

    private ListView list_package;
    //Database
    private CompositeDisposable compositeDisposable;
    private PackageRepository packageRepository;

    //Adapter
    private List<Package> packageList = new ArrayList<Package>();
    private ArrayAdapter adapter;





    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_choice_for_client);
        final Intent intent = getIntent();
        //intent.putExtra("PackageForClient", (Serializable) packages);

        compositeDisposable = new CompositeDisposable();

        //Init View
        list_package = (ListView) findViewById(R.id.package_list_client);

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
                Intent intent1 = new Intent(ListPackageForClient.this, AddClient.class);
                intent1.putExtra("serviceForPackage", (Serializable) packages);
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
                                Toast.makeText(ListPackageForClient.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
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
