package com.android.skripsi.carikuliner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
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
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setTitle("Hasil Rekomendasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultNama = findViewById(R.id.txtNamaResult);
        resultJarak = findViewById(R.id.txtJarakResult);
        btnViewDetail = findViewById(R.id.btnToDetail);
        btnToMaps = findViewById(R.id.btnToGMaps);
        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetail = new Intent(MapsActivity.this, DetailActivity.class);
                startActivityForResult(toDetail, 1);
            }
        });
        getPermission();
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

    private void initLoc(){
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(isPermissionGranted){
            getData();
        }else{
            Log.d("isPermissionGranted", "Maps cannot be accessed due to no premission to access location from device");
        }
    }

    private void getData(){
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
        String weight = shared.getString("weight", "");
        String choice = shared.getString("choices", "");;
        final Call<GetRekomendasi> rekomendasiCall = apiInterface.getRekomendasi(latUser, _longUser, weight, choice);
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
            }
        });
    }

    private void updateUI(Rekomendasi rekomendasi){
        LatLng places = new LatLng(Double.parseDouble(rekomendasi.getLatTempat()), Double.parseDouble(rekomendasi.getLonTempat()));
        mMap.addMarker(new MarkerOptions().position(places).title(rekomendasi.getNamaTempat()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(places, 15f));
        resultNama.setText(rekomendasi.getNamaTempat());
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.CEILING);
        double jarak = rekomendasi.getJarak();
        resultJarak.setText("Sekitar " + String.valueOf(format.format(jarak)) + " meter dari posisimu saat ini");
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
                    Toast.makeText(MapsActivity.this, "Aplikasi tidak dapat berjalan karena sistem tidak dapat mendapatkan izin untuk mengakses lokasi dari perangkat", Toast.LENGTH_SHORT).show();
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