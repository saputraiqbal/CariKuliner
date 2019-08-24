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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.skripsi.carikuliner.adapter.AdapterResult;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationActivity extends AppCompatActivity implements OnMapReadyCallback, UIInterface, DialogInterface.OnClickListener, ConnectionInterface, ConnectionInterface.GPS {
    private ApiInterface apiInterface;
    private SupportMapFragment mapFrag;
    private FusedLocationProviderClient locFused;
    private GoogleMap mMap;
    private static boolean isPermissionGranted = false;
    private RecyclerView rvResult;
    protected List<Rekomendasi> rekomendasis = new ArrayList<>();
    protected Location userLoc = null;
    protected int currentPosRv = 0;
    AlertDialog dialogTimeout, dialogNoConnection, dialogNoGPS, dialogBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rekomendasi Untukmu");
        checkConnection(this);
    }

    //initiate Map Fragment
    private void initLoc(){
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(RecommendationActivity.this);
    }

    // add markers for each recommendations
    private void addMarkers(List<Rekomendasi> rekomendasis) {
        int i = 0;
        LatLng places = null;
        do{
            switch (i){
                case 0:
                    places = new LatLng(Double.parseDouble(rekomendasis.get(i).getLatTempat()), Double.parseDouble(rekomendasis.get(i).getLonTempat()));
                    mMap.addMarker(new MarkerOptions()
                            .position(places)
                            .title(rekomendasis.get(i).getNamaTempat())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gold)));
                    break;
                case 1:
                    places = new LatLng(Double.parseDouble(rekomendasis.get(i).getLatTempat()), Double.parseDouble(rekomendasis.get(i).getLonTempat()));
                    mMap.addMarker(new MarkerOptions()
                            .position(places)
                            .title(rekomendasis.get(i).getNamaTempat())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_silver)));
                    break;
                case 2:
                    places = new LatLng(Double.parseDouble(rekomendasis.get(i).getLatTempat()), Double.parseDouble(rekomendasis.get(i).getLonTempat()));
                    mMap.addMarker(new MarkerOptions()
                            .position(places)
                            .title(rekomendasis.get(i).getNamaTempat())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bronze)));
                    break;
            }
            i++;
            }while (i != rekomendasis.size());
        }

    //get permission to access GPS on smartphone
    private void getPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }else{
            isPermissionGranted = true;
            initLoc();
        }
    }

    //set Map when ready
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

    //get user's current location
    private void getKoordinat(){
        locFused = LocationServices.getFusedLocationProviderClient(this);
        try{
            final Task userLocation = locFused.getLastLocation();
            userLocation.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && task.getResult() != null){
                        Log.d("GetLocation", "User location is found");
                        userLoc = (Location)task.getResult();
                        loadData();
                    }
                }
            });
        }catch (SecurityException e){
            Log.d("SecurityException", e.getMessage());
        }
    }

    @Override
    public void setupUI() {
        setContentView(R.layout.activity_recommendation);
        rvResult = findViewById(R.id.rv_result);
        rvResult.setHasFixedSize(true);
    }

    @Override
    public void loadData() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final SharedPreferences shared = getSharedPreferences("value_stores", MODE_PRIVATE);
        double latUser = userLoc.getLatitude();
        double _longUser = userLoc.getLongitude();
        String category = shared.getString("cat", "0");
        final Call<GetRekomendasi> rekomendasiCall = apiInterface.getRekomendasi(latUser, _longUser, category, "1-1-1-1");
        final Context ctx = this.getApplicationContext();
        rekomendasiCall.enqueue(new Callback<GetRekomendasi>() {
            @Override
            public void onResponse(Call<GetRekomendasi> call, Response<GetRekomendasi> response) {
                Log.d("Success", "Object : " + response.body().getResult().toString());
                rekomendasis.addAll(response.body().getResult());
                updateUI();
            }
            @Override
            public void onFailure(Call<GetRekomendasi> call, Throwable t) {
                Log.d("Error", "Error : " + t.getMessage());
                alertTimeout();
            }
        });
    }

    @Override
    public void updateUI() {
        final LinearLayoutManager rvManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvResult.setLayoutManager(rvManager);
        //add SnapHelper to make sure RecyclerView is snapped on an item when user is swiping the RecyclerView
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvResult);
        AdapterResult adapter = new AdapterResult(rekomendasis);
        rvResult.setAdapter(adapter);
        addMarkers(rekomendasis);
        Log.d("currentPosition", "Current position is " + currentPosRv);
        LatLng firstPlaces = new LatLng(Double.parseDouble(rekomendasis.get(0).getLatTempat()), Double.parseDouble(rekomendasis.get(0).getLonTempat()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstPlaces, 15f));

        //listener to make sure map camera is moving when user is scrolling the RecyclerView
        rvResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    currentPosRv = rvManager.findFirstCompletelyVisibleItemPosition();
                    Log.d("currentPosition", "Current position is " + currentPosRv);
                    LatLng currentPlaces = new LatLng(Double.parseDouble(rekomendasis.get(currentPosRv).getLatTempat()), Double.parseDouble(rekomendasis.get(currentPosRv).getLonTempat()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPlaces, 15f));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void checkConnection(Context ctx) {
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

    @Override
    public void alertTimeout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_timeout)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .setCancelable(false);
        dialogTimeout = builder.create();
        dialogTimeout.show();
    }

    @Override
    public void alertNoGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_no_gps)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .setCancelable(false);
        dialogNoGPS = builder.create();
        dialogNoGPS.show();
    }

    @Override
    public void alertNoConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_no_connection)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .setCancelable(false);
        dialogNoConnection = builder.create();
        dialogNoConnection.show();
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
                    Toast.makeText(RecommendationActivity.this, "Aplikasi tidak dapat berjalan karena sistem tidak dapat mendapatkan izin untuk mengakses lokasi dari perangkat", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.ask_back)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this);
        dialogBack = builder.create();
        dialogBack.show();
    }

    @Override
    public void onBackPressed() {
        askBack();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == dialogTimeout){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    getPermission();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Intent goBack = new Intent(RecommendationActivity.this, HomeActivity.class);
                    startActivityForResult(goBack, 1);
                    finish();
                    break;
            }
        }else if(dialog == dialogNoGPS || dialog == dialogNoConnection){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    checkConnection(RecommendationActivity.this);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }else if(dialog == dialogBack){
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
    }
}