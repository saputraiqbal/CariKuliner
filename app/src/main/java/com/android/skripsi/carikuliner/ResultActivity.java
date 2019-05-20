package com.android.skripsi.carikuliner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback{
    private ApiInterface apiInterface;
    private SupportMapFragment mapFrag;
    private FusedLocationProviderClient locFused;
    private GoogleMap mMap;
    private static boolean isPermissionGranted = false;
    TextView resultNama, resultJarak;
    Button btnViewDetail, btnToMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rekomendasi Untukmu");
        checkConnection(this);
    }

    private void initLoc(){
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(ResultActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(isPermissionGranted){
            getKoordinat();
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }else{
            Log.d("isPermissionGranted", "Maps cannot be accessed due to no premission to access location from device");
        }
    }

    private void getKoordinat(){
        locFused = LocationServices.getFusedLocationProviderClient(this);
        try{
            final Task userLocation = locFused.getLastLocation();
            userLocation.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && task.getResult() != null){
                        Log.d("GetLocation", "User location is found");
                        Location userLoc = (Location)task.getResult();
                        getRekomendasi(new LatLng(userLoc.getLatitude(), userLoc.getLongitude()));
                    }
                }
            });
        }catch (SecurityException e){
            Log.d("SecurityException", e.getMessage());
        }
    }

    private void getRekomendasi(LatLng coordinate){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final SharedPreferences shared = getSharedPreferences("value_stores", MODE_PRIVATE);
        double latUser = coordinate.latitude;
        double _longUser = coordinate.longitude;
        String category = shared.getString("cat", "0");;
        final Call<GetRekomendasi> rekomendasiCall = apiInterface.getRekomendasi(latUser, _longUser, category, "1-1-1-1");
        rekomendasiCall.enqueue(new Callback<GetRekomendasi>() {
            @Override
            public void onResponse(Call<GetRekomendasi> call, Response<GetRekomendasi> response) {
                Log.d("Success", "Object : " + response.body().getResult().toString());
                SharedPreferences shares = getSharedPreferences("value_stores", MODE_PRIVATE);
                SharedPreferences.Editor editor = shares.edit();
                editor.putString("idChosen", response.body().getResult().getId());
                editor.putString("jarak", String.valueOf(response.body().getResult().getJarak()));
                editor.commit();
                updateUI(response.body().getResult());
            }
            @Override
            public void onFailure(Call<GetRekomendasi> call, Throwable t) {
                Log.d("Error", "Error : " + t.getMessage());
                alertTimeout();
            }
        });
    }

    private void setupUI(){
        setContentView(R.layout.activity_maps);

        resultNama = findViewById(R.id.txtNamaResult);
        resultJarak = findViewById(R.id.txtJarakResult);
        btnViewDetail = findViewById(R.id.btnToDetail);
        btnToMaps = findViewById(R.id.btnToGMaps);
        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetail = new Intent(ResultActivity.this, DetailActivity.class);
                startActivityForResult(toDetail, 1);
            }
        });
    }


    private void checkConnection(Context ctx){
        ConnectivityManager conManager = (ConnectivityManager)ctx.getSystemService(CONNECTIVITY_SERVICE);
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            alertNoGPS();
        }else{
            if (conManager != null){
                NetworkInfo activeNet = conManager.getActiveNetworkInfo();
                if((activeNet != null) && (activeNet.isConnectedOrConnecting())){
                    setupUI();
                    getPermission();
                }else{
                    alertNoConnection();
                }
            }
        }
    }

    private void alertTimeout(){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        getPermission();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Intent goBack = new Intent(ResultActivity.this, HomeActivity.class);
                        startActivityForResult(goBack, 1);
                        finish();
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Ada masalah saat terhubung dengan layanan. Ingin coba lagi?")
                .setPositiveButton("YA", dialogListener)
                .setNegativeButton("TIDAK", dialogListener)
                .setCancelable(false)
                .show();
    }

    private void alertNoGPS(){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        checkConnection(ResultActivity.this);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Saat ini Anda tidak terhubung dengan GPS. Mohon sambungkan perangkat Anda ke GPS")
                .setPositiveButton("YA", dialogListener)
                .setNegativeButton("TIDAK", dialogListener)
                .setCancelable(false)
                .show();
    }

    private void alertNoConnection(){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        checkConnection(ResultActivity.this);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Saat ini Anda tidak terhubung dengan internet. Mohon sambungkan perangkat Anda ke internet")
                .setPositiveButton("YA", dialogListener)
                .setNegativeButton("TIDAK", dialogListener)
                .setCancelable(false)
                .show();
    }

    private void updateUI(Rekomendasi rekomendasi){
        LatLng places = new LatLng(Double.parseDouble(rekomendasi.getLatTempat()), Double.parseDouble(rekomendasi.getLonTempat()));
        mMap.addMarker(new MarkerOptions().position(places).title(rekomendasi.getNamaTempat()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(places, 15f));
        resultNama.setText(rekomendasi.getNamaTempat());
        DecimalFormat format = new DecimalFormat("#,##");
        format.setRoundingMode(RoundingMode.CEILING);
        double jarak = rekomendasi.getJarak();
        resultJarak.setText("Sekitar " + String.valueOf(format.format(jarak)) + " km dari posisimu saat ini");
        final String uriLocs = "http://maps.google.com/maps?daddr=" + rekomendasi.getLatTempat() + "," + rekomendasi.getLonTempat();
        btnToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("uriLocs", uriLocs);
                Intent toMaps = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriLocs));
                if (toMaps.resolveActivity(getPackageManager()) != null){
                    startActivity(toMaps);
                }else{
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ResultActivity.this);
                    dialogBuilder.setMessage("Aplikasi Google Maps belum terpasang pada perangkat Anda. Mohon pasang terlebih dahulu untuk mendapatkan petunjuk arah.")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }
            }
        });
    }


    private void getPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }else{
            isPermissionGranted = true;
            initLoc();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            isPermissionGranted = false;
                            Log.d("isPermissionGranted", "Permission is not granted");
                            return;
                        }
                    }
                    isPermissionGranted = true;
                    Log.d("isPermissionGranted", "Permission is granted");
                    initLoc();
                }else{
                    isPermissionGranted = false;
                    Log.d("isPermissionGranted", "Permission is not granted");
                    Toast.makeText(ResultActivity.this, "Aplikasi tidak dapat berjalan karena sistem tidak dapat mendapatkan izin untuk mengakses lokasi dari perangkat", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                askBack();
                break;
        }
        return false;
    }

    public void askBack(){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent goBack = new Intent();
                        goBack.putExtra("backPressed", true);
                        setResult(Activity.RESULT_OK, goBack);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Apakah Anda mau kembali?")
                .setPositiveButton("YA", dialogListener)
                .setNegativeButton("TIDAK", dialogListener).show();
    }

    @Override
    public void onBackPressed() {
        askBack();
    }
}