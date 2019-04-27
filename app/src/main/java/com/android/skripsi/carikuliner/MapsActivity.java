package com.android.skripsi.carikuliner;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity{
    private GoogleMap mMap;
    private FusedLocationProviderClient locFused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locFused = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng place = new LatLng(0, 0);
                float zoom = (float) 15.0f;
                mMap.addMarker(new MarkerOptions().position(place).title("Hey, guys! :)"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place, zoom));
            }
        });
    }
}
