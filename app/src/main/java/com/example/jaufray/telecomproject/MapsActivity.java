package com.example.jaufray.telecomproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        // Add a marker in Sierre and move the camera
        LatLng sierre = new LatLng(46.292868, 7.535923);
        mMap.addMarker(new MarkerOptions().position(sierre).title("Telecommuncation HES - Sierre").snippet("3960 Sierre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sierre));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sierre, 10.0f));

        // Add a marker in Sion and move the camera
        LatLng sion = new LatLng(46.243065, 7.362709);
        mMap.addMarker(new MarkerOptions().position(sion).title("Telecommuncation HES - Sion").snippet("1950 Sion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sion));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sion, 10.0f));


    }
}
