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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihKategoriActivity extends AppCompatActivity implements ConnectionInterface, GetDataInterface, DialogInterface.OnClickListener {

    ApiInterface apiInterface;
    private RecyclerView rvData;
    private RecyclerView.Adapter adapterData;
    private RecyclerView.LayoutManager rvManager;
    protected List<Kategori> kategori = new ArrayList<>();
    AlertDialog dialogTimeout, dialogNoConnection, dialogBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pilih Kategori");
        checkConnection(this);
    }

    @Override
    public void setupUI() {
        setContentView(R.layout.activity_pilih_kategori);

        rvData = findViewById(R.id.rvData);
        rvManager = new LinearLayoutManager(this);
        rvData.setLayoutManager(rvManager);
    }

    @Override
    public void loadData() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetKategori> getAlternatifCall = apiInterface.getKategori();
        getAlternatifCall.enqueue(new Callback<GetKategori>() {
            @Override
            public void onResponse(Call<GetKategori> call, Response<GetKategori> response) {
                kategori.addAll(response.body().getKategoriList());
                updateUI();
                Log.d("result", "Sukses");
            }

            @Override
            public void onFailure(Call<GetKategori> call, Throwable t) {
                Log.d("Results", t.toString());
                alertTimeout();
            }
        });
    }

    @Override
    public void updateUI() {
        rvData.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        adapterData = new AdapterKategori(kategori);
        rvData.setAdapter(adapterData);
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
    public void alertTimeout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_timeout)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .setCancelable(false);
        dialogTimeout = builder.create();
        dialogTimeout.show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                askBack();
                break;
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == dialogNoConnection){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    checkConnection(PilihKategoriActivity.this);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }else if (dialog == dialogTimeout){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    loadData();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Intent goBack = new Intent(PilihKategoriActivity.this, HomeActivity.class);
                    startActivityForResult(goBack, 1);
                    finish();
                    break;
            }
        }else if (dialog == dialogBack){
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
