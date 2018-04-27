package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Database.PackageServiceJoinRepository;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.PackageServiceJoinDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
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

public class ListPackages extends Activity{

    private ListView list_packages;

    //Database
    private CompositeDisposable compositeDisposable;
    private PackageRepository packageRepository;

    //Adapter
    List<Package> packageList = new ArrayList<>();
    ArrayAdapter adapter;

    private ArrayList<Service> listService = new ArrayList<Service>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        Intent intent = getIntent();

        // Init
        compositeDisposable = new CompositeDisposable();

        // Init View
        list_packages = (ListView) findViewById(R.id.full_package_list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, packageList);
        registerForContextMenu(list_packages);
        list_packages.setAdapter(adapter);

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));

        //Load all data from DB
        loadData();

        list_packages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Package packages = (Package) adapterView.getAdapter().getItem(i);

                Intent intent1 = new Intent(ListPackages.this, DetailsPackage.class);
                intent1.putExtra("DetailPackages", packages);
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
                                Toast.makeText(ListPackages.this, "" + throwable.getMessage() , Toast.LENGTH_SHORT).show();
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

    public void changeToCreatePackage(View view){
        Intent intent = new Intent(ListPackages.this, AddPackage.class);
        startActivity(intent);
        finish();
    }

}
