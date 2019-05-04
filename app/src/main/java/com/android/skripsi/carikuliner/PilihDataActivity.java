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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.skripsi.carikuliner.adapter.AdapterData;
import com.android.skripsi.carikuliner.model.Alternatif;
import com.android.skripsi.carikuliner.model.SelectedAlternatif;
import com.android.skripsi.carikuliner.rest.ApiClient;
import com.android.skripsi.carikuliner.rest.ApiInterface;
import com.android.skripsi.carikuliner.model.GetAlternatif;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihDataActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    private RecyclerView rvData;
    private RecyclerView.Adapter adapterData;
    private RecyclerView.LayoutManager rvManager;
    public List<SelectedAlternatif> selected = new ArrayList<>();
    Button btnSave, btnChooseAll;
    public boolean isChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection(this);
    }

    private void setupUI(){
        setContentView(R.layout.activity_pilih_data);
        getSupportActionBar().setTitle("Pilih Tempat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave = findViewById(R.id.btnConfirmChoose);
        btnChooseAll = findViewById(R.id.btnChooseAll);
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
                        checkConnection(PilihDataActivity.this);
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
        Call<GetAlternatif> getAlternatifCall = apiInterface.getDataAll();
        getAlternatifCall.enqueue(new Callback<GetAlternatif>() {
            @Override
            public void onResponse(Call<GetAlternatif> call, Response<GetAlternatif> response) {
                List<Alternatif> alternatif = response.body().getAlternatifList();
                adapterData = new AdapterData(alternatif, new AdapterData.OnCheckedItemListener() {
                    @Override
                    public void onCheckedItem(String item) {
                        SelectedAlternatif itemSelect = new SelectedAlternatif();
                        itemSelect.setIdSelected(item);
                        selected.add(itemSelect);
                        Log.d("Message Added", item + " was added");
                    }

                    @Override
                    public void onUncheckedItem(String item) {
                        for(int i = 0; i < selected.size(); i++){
                            if(selected.get(i).getIdSelected() == item){
                                selected.remove(i);
                                break;
                            }
                        }
                        Log.d("Message Removed", item + " was removed");
                    }
                });
                rvData.setAdapter(adapterData);
                Log.d("Results", "Hasil : " + String.valueOf(adapterData.getItemCount()));

                btnChooseAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isChecked){
                            ((AdapterData)adapterData).setSelectedAll();
                            btnChooseAll.setText("HAPUS SEMUA");
                            isChecked = true;
                        }else{
                            ((AdapterData)adapterData).setUnselectedAll();
                            btnChooseAll.setText("PILIH SEMUA");
                            isChecked = false;
                        }
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String dataChosen = "";
                        for (int i = 0; i < selected.size(); i++){
                            dataChosen += selected.get(i).getIdSelected();
                            if(i < selected.size() - 1)
                                dataChosen += "-";
                            Log.d("View Data Enter", "Data ke- " + i + " yang masuk : " + selected.get(i).getIdSelected());
                        }
                        SharedPreferences shares = getSharedPreferences("value_stores", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shares.edit();
                        editor.putString("choices", dataChosen);
                        editor.commit();
                        Log.d("itemStored", "[" + shares.getString("choices", dataChosen) + "] was added to SharedPreferences");
                        Log.d("Result", "Banyaknya yang dipilih : " + String.valueOf(selected.size()));
                        Intent toResult = new Intent(PilihDataActivity.this, MapsActivity.class);
                        startActivityForResult(toResult, 1);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetAlternatif> call, Throwable t) {
                Log.d("Results", t.toString());
            }
        });
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
        dialogBuilder.setMessage("Apakah Anda mau kembali? Anda harus set nilai bobot dari awal lagi")
                .setPositiveButton("YA", dialogListener)
                .setNegativeButton("TIDAK", dialogListener).show();
    }
}
