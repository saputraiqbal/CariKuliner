package com.android.skripsi.carikuliner;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.skripsi.carikuliner.model.GetRekomendasi;
import com.android.skripsi.carikuliner.model.Rekomendasi;
import com.android.skripsi.carikuliner.rest.ApiClient;
import com.android.skripsi.carikuliner.rest.ApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity{
    private ApiInterface apiInterface;
    private GoogleMap mMap;
    public double latCustom;
    public double longCostum;
    Rekomendasi rekomendasi = new Rekomendasi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        load(new GetDataInterface() {
            @Override
            public void onGetData(@NonNull Rekomendasi result) {
                rekomendasi = result;
                latCustom = Double.parseDouble(rekomendasi.getLatTempat());
                longCostum = Double.parseDouble(rekomendasi.getLonTempat());
                SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFrag.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        LatLng place = new LatLng(latCustom, longCostum);
                        float zoom = (float) 15.0f;
                        mMap.addMarker(new MarkerOptions().position(place).title("Hey, guys! :)"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place, zoom));
                    }
                });
            }
        });
    }

    public void load(@Nullable final GetDataInterface callback){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetRekomendasi> rekomendasiCall = apiInterface.getRekomendasi(String.valueOf(latCustom), String.valueOf(longCostum), "3-5-3-1", "3-4-5");
        rekomendasiCall.enqueue(new Callback<GetRekomendasi>() {
            @Override
            public void onResponse(Call<GetRekomendasi> call, Response<GetRekomendasi> response) {
                Log.d("Success", "Object : " + response.body().getResult().toString());
                if(callback != null)
                    callback.onGetData(response.body().getResult());
            }
            @Override
            public void onFailure(Call<GetRekomendasi> call, Throwable t) {
                Log.d("Error", "Error : " + t.toString());
            }
        });
    }
}
