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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.android.skripsi.carikuliner.adapter.AdapterKategori;
import com.android.skripsi.carikuliner.model.Kategori;
import com.android.skripsi.carikuliner.rest.ApiClient;
import com.android.skripsi.carikuliner.rest.ApiInterface;
import com.android.skripsi.carikuliner.model.GetKategori;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihKategoriActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    private RecyclerView rvData;
    private RecyclerView.Adapter adapterData;
    private RecyclerView.LayoutManager rvManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pilih Kategori");
        checkConnection(this);
    }

    private void setupUI(){
        setContentView(R.layout.activity_pilih_kategori);

        rvData = (RecyclerView) findViewById(R.id.rvData);
        rvManager = new LinearLayoutManager(this);
        rvData.setLayoutManager(rvManager);
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
                        checkConnection(PilihKategoriActivity.this);
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

    public void load(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetKategori> getAlternatifCall = apiInterface.getKategori();
        getAlternatifCall.enqueue(new Callback<GetKategori>() {
            @Override
            public void onResponse(Call<GetKategori> call, Response<GetKategori> response) {
                List<Kategori> kategori = response.body().getKategoriList();
                rvData.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                adapterData = new AdapterKategori(kategori);
                rvData.setAdapter(adapterData);
                Log.d("result", "Sukses");
            }

            @Override
            public void onFailure(Call<GetKategori> call, Throwable t) {
                Log.d("Results", t.toString());
                alertTimeout();
            }
        });
    }

    private void alertTimeout(){
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        load();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Intent goBack = new Intent(PilihKategoriActivity.this, HomeActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                askBack();
                break;
        }
        return false;
    }
}
