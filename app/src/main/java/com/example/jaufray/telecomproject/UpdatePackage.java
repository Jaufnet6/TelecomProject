package com.example.jaufray.telecomproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaufray.telecomproject.Database.IPackageServiceJoinDataSource;
import com.example.jaufray.telecomproject.Database.PackageRepository;
import com.example.jaufray.telecomproject.Database.PackageServiceJoinRepository;
import com.example.jaufray.telecomproject.Database.ServiceRepository;
import com.example.jaufray.telecomproject.Local.PackageDataSource;
import com.example.jaufray.telecomproject.Local.PackageServiceJoinDataSource;
import com.example.jaufray.telecomproject.Local.TelecomDatabase;
import com.example.jaufray.telecomproject.Model.Package;
import com.example.jaufray.telecomproject.Model.PackageServiceJoin;
import com.example.jaufray.telecomproject.Model.Service;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpdatePackage extends AppCompatActivity {

    private EditText edtName;
    private ListView service_list;
    private EditText edtPrice;

    private String namePackage;
    private ArrayList<Service> listService;
    private Integer pricePackage;

    private Package packages;

    private ArrayAdapter adapter;

    private CompositeDisposable compositeDisposable;
    private PackageRepository packageRepository;
    private PackageServiceJoinRepository packageServiceJoinRepository;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_edit);
        Intent intent = getIntent();

        edtName = (EditText) findViewById(R.id.package_title);
        edtPrice = (EditText) findViewById(R.id.package_price_id);
        service_list = (ListView) findViewById(R.id.service_list_in_package);

        packages = (Package) intent.getSerializableExtra("packageToModify");
        listService = (ArrayList<Service>) intent.getSerializableExtra("serviceForPackage");
        namePackage = (String) intent.getStringExtra("packageName");
        pricePackage = (Integer) intent.getIntExtra("packagePrice", 0);

        if(namePackage != null)
            edtName.setText(namePackage);
        else
            edtName.setText(packages.getName());
        if(pricePackage != 0)
            edtPrice.setText(String.valueOf(pricePackage));
        else
            edtPrice.setText(String.valueOf(packages.getPrice()));



        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listService);
        registerForContextMenu(service_list);
        service_list.setAdapter(adapter);


        // Init
        compositeDisposable = new CompositeDisposable();

        //Database
        TelecomDatabase telecomDatabase = TelecomDatabase.getInstance(this); //Create database
        packageRepository = PackageRepository.getInstance(PackageDataSource.getInstance(telecomDatabase.packageDAO()));
        packageServiceJoinRepository = PackageServiceJoinRepository.getInstance(PackageServiceJoinDataSource.getInstance(telecomDatabase.packageServiceJoinDAO()));


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action :");
        menu.add(Menu.NONE,0, Menu.NONE, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Service service = listService.get(info.position);

        switch (item.getItemId())
        {
            case 0: //Delete
            {
                new AlertDialog.Builder(UpdatePackage.this)
                        .setMessage("Do you want to delete ? " + service.getName().toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listService.remove(service);
                                adapter.notifyDataSetChanged();
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

    public void savePackageUpdate(View view){


        namePackage = edtName.getText().toString();


        try{
            pricePackage = Integer.parseInt(edtPrice.getText().toString());
        } catch (NumberFormatException ex){
            int total = 0;
            for(Service s : listService)
                total =+ s.getPrice();
            pricePackage = total;
        }



        if(TextUtils.isEmpty(namePackage)) {
            edtName.setError("Cannot be empty");
            return;
        }

        if(pricePackage == 0){
            Toast.makeText(UpdatePackage.this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }



        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                deleteLinkPackageToService();

                packages.setName(namePackage);
                packages.setPrice(pricePackage);
                packageRepository.updatePackage(packages);
                e.onComplete();
                addDataLinkService();
            }

        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(UpdatePackage.this, "Package added!", Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(UpdatePackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ,

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(UpdatePackage.this, ListPackages.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                );

        this.finish();
    }

    public void addDataLinkService(){

        int id = packages.getId();


        for(Service s : listService){
            final PackageServiceJoin packServ = new PackageServiceJoin(id, s.getId());


            Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {

                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    packageServiceJoinRepository.insert(packServ);
                    e.onComplete();
                }

            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer() {
                                   @Override
                                   public void accept(Object o) throws Exception {
                                       Toast.makeText(UpdatePackage.this, "Package added!", Toast.LENGTH_SHORT).show();
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(UpdatePackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            ,

                            new Action() {
                                @Override
                                public void run() throws Exception {
                                }
                            }
                    );

        }
    }

    public void addServicesToPackage(View view) {

        namePackage = edtName.getText().toString();
        pricePackage = Integer.parseInt(edtPrice.getText().toString());

        Intent intent = new Intent(UpdatePackage.this, EditListServiceForPackage.class);
        intent.putExtra("serviceForPackage", (Serializable) listService);
        intent.putExtra("packageToEdit", packages);
        intent.putExtra("packageName", namePackage);
        intent.putExtra("packagePrice", pricePackage);

        startActivity(intent);
        finish();


    }

    public void cancelPackageUpdate (View view){
        Intent intent = new Intent(UpdatePackage.this, ListPackages.class);
        startActivity(intent);
        this.finish();
    }

    public void deletePackageButton(View view){
        new AlertDialog.Builder(UpdatePackage.this)
                .setMessage("Do you want to delete ? " + packages.getName().toString())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePackage();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void deletePackage(){

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {


            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                deleteLinkPackageToService();
                try{
                    Thread.sleep(50);
                }catch (Exception ex){

                }
                packageRepository.deletePackage(packages);
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
                                Toast.makeText(UpdatePackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(UpdatePackage.this, ListPackages.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                );

        compositeDisposable.add(disposable);

    }

    private void deleteLinkPackageToService() {

        for(Service s : listService){

            final PackageServiceJoin packServ = new PackageServiceJoin(packages.getId(), s.getId());

            Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {


                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    packageServiceJoinRepository.deletePackageServiceJoin(packServ);
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
                                    Toast.makeText(UpdatePackage.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },

                            new Action() {
                                @Override
                                public void run() throws Exception {
                                }
                            }

                    );

            compositeDisposable.add(disposable);

        }


    }
}
