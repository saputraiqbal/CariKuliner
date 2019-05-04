package com.android.skripsi.carikuliner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
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

public class DetailActivity extends AppCompatActivity {

    TextView namaTempat, jarakTempat, lokasi, harga, rating, usia, tglBerdiri;
    RatingBar ratingBar;
    ApiInterface apiInterface;
    private DetailInfo detail = new DetailInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection(this);
    }

    private void checkConnection(Context ctx){
        ConnectivityManager conManager = (ConnectivityManager)ctx.getSystemService(CONNECTIVITY_SERVICE);
        if (conManager != null){
            NetworkInfo activeNet = conManager.getActiveNetworkInfo();
            if((activeNet != null) && (activeNet.isConnectedOrConnecting())){
                setupUI();
                load();
            }else{
                alertNoConnection();
            }
        }
    }

    private void alertNoConnection(){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        checkConnection(DetailActivity.this);
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

    private void load(){
        SharedPreferences shared = getSharedPreferences("value_stores", MODE_PRIVATE);
        String id = shared.getString("idChosen", "");
        final String jarak = shared.getString("jarak", "0.00");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetDetail> getDetailCall = apiInterface.getDetail(id);
        getDetailCall.enqueue(new Callback<GetDetail>() {
            @Override
            public void onResponse(Call<GetDetail> call, Response<GetDetail> response) {
                Log.d("GetData", response.body().getDetailInfo().toString() + " has been obtained");
                detail = response.body().getDetailInfo();
                updateUI(detail, jarak);
            }

            @Override
            public void onFailure(Call<GetDetail> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void setupUI(){
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Detail Tempat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaTempat = findViewById(R.id.txtNamaTempat);
        jarakTempat = findViewById(R.id.txtJarakTempat);
        lokasi = findViewById(R.id.txtLokasi);
        harga = findViewById(R.id.txtHarga);
        rating = findViewById(R.id.txtRating);
        ratingBar = findViewById(R.id.ratingBar);
        usia = findViewById(R.id.txtUsia);
        tglBerdiri = findViewById(R.id.txtTglBerdiri);
    }

    private void updateUI(DetailInfo result, String jarak_temp){
        DecimalFormat format = new DecimalFormat("#,##");
        format.setRoundingMode(RoundingMode.CEILING);
        double jarak = Double.parseDouble(jarak_temp);
        float valRating = Float.parseFloat(result.getRating());
        int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
        int yearPlace = Integer.parseInt(result.getTahunBerdiri());
        int age = yearCurrent - yearPlace;
        namaTempat.setText(result.getNamaTempat());
        jarakTempat.setText(format.format(jarak) + " km dari posisimu saat ini");
        lokasi.setText(result.getAlamat());
        harga.setText("Mulai dari Rp " + result.getHarga());
        rating.setText(result.getRating() + "/5");
        ratingBar.setRating(valRating);
        usia.setText("sejak " + String.valueOf(age) + " tahun yang lalu");
        tglBerdiri.setText("Berdiri pada tahun " + result.getTahunBerdiri());
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
}
