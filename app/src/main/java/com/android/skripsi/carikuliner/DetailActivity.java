package com.android.skripsi.carikuliner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.skripsi.carikuliner.model.DetailInfo;
import com.android.skripsi.carikuliner.model.GetDetail;
import com.android.skripsi.carikuliner.rest.ApiClient;
import com.android.skripsi.carikuliner.rest.ApiInterface;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements GetDataInterface, ConnectionInterface, DialogInterface.OnClickListener{

    TextView namaTempat, jarakTempat, lokasi, harga, rating, usia, tglBerdiri;
    RatingBar ratingBar;
    ApiInterface apiInterface;
    static AlertDialog dialogNoConnection, dialogTimeout;
    private DetailInfo detail = new DetailInfo();
    private String id;
    private double jarak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Info Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkConnection(this);
    }

    @Override
    public void setupUI() {
        setContentView(R.layout.activity_detail);

        namaTempat = findViewById(R.id.txtNamaTempat);
        jarakTempat = findViewById(R.id.txtJarakTempat);
        lokasi = findViewById(R.id.txtLokasi);
        harga = findViewById(R.id.txtHarga);
        rating = findViewById(R.id.txtRating);
        ratingBar = findViewById(R.id.ratingBar);
        usia = findViewById(R.id.txtUsia);
        tglBerdiri = findViewById(R.id.txtTglBerdiri);
    }

    @Override
    public void loadData() {
        Bundle getBundle = getIntent().getExtras();
        id = getBundle.getString("id");
        jarak = getBundle.getDouble("jarak");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetDetail> getDetailCall = apiInterface.getDetail(id);
        getDetailCall.enqueue(new Callback<GetDetail>() {
            @Override
            public void onResponse(Call<GetDetail> call, Response<GetDetail> response) {
                Log.d("GetData", response.body().getDetailInfo().toString() + " has been obtained");
                detail = response.body().getDetailInfo();
                updateUI();
            }

            @Override
            public void onFailure(Call<GetDetail> call, Throwable t) {
                Log.d("Error", t.getMessage());
                alertTimeout();
            }
        });
    }

    @Override
    public void updateUI() {
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.CEILING);
        float valRating = Float.parseFloat(detail.getRating());
        int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
        int yearPlace = Integer.parseInt(detail.getTahunBerdiri());
        int age = yearCurrent - yearPlace;
        namaTempat.setText(detail.getNamaTempat());
        jarakTempat.setText(format.format(jarak) + " km dari posisimu saat ini");
        lokasi.setText(detail.getAlamat());
        harga.setText("Mulai dari Rp " + detail.getHarga());
        rating.setText(detail.getRating() + "/5");
        ratingBar.setRating(valRating);
        usia.setText("sejak " + String.valueOf(age) + " tahun yang lalu");
        tglBerdiri.setText("Berdiri pada tahun " + detail.getTahunBerdiri());
    }

    @Override
    public void checkConnection(Context ctx) {
        ConnectivityManager conManager = (ConnectivityManager)ctx.getSystemService(CONNECTIVITY_SERVICE);
        if (conManager != null){
            NetworkInfo activeNet = conManager.getActiveNetworkInfo();
            if((activeNet != null) && (activeNet.isConnectedOrConnecting())){
                setupUI();
                loadData();
            }else{
                alertNoConnection();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                askBack();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        askBack();
    }

    public void askBack(){
        Intent goBack = new Intent();
        goBack.putExtra("backPressed", true);
        setResult(Activity.RESULT_OK, goBack);
        finish();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == dialogTimeout){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    loadData();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Intent goBack = new Intent(DetailActivity.this, HomeActivity.class);
                    startActivityForResult(goBack, 1);
                    finish();
                    break;
            }
        }else if (dialog == dialogNoConnection){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    checkConnection(DetailActivity.this);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
