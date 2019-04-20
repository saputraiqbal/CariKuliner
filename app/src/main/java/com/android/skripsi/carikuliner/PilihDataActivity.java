package com.android.skripsi.carikuliner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.skripsi.carikuliner.adapter.AdapterData;
import com.android.skripsi.carikuliner.model.Alternatif;
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
    public static PilihDataActivity pdAct;
    String bobot = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_data);

        final Bundle fromBobot = this.getIntent().getExtras();
        bobot = fromBobot.getString("bobot");

        rvData = (RecyclerView) findViewById(R.id.rvData);
        rvManager = new LinearLayoutManager(this);
        rvData.setLayoutManager(rvManager);

        pdAct = this;
        load();
    }

    public void load(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetAlternatif> getAlternatifCall = apiInterface.getDataAll();
        getAlternatifCall.enqueue(new Callback<GetAlternatif>() {
            @Override
            public void onResponse(Call<GetAlternatif> call, Response<GetAlternatif> response) {
                List<Alternatif> alternatif = response.body().getAlternatifList();
                Log.d("Results", "Hasil : " + String.valueOf(alternatif.size()));
                adapterData = new AdapterData(alternatif);
                rvData.setAdapter(adapterData);
            }

            @Override
            public void onFailure(Call<GetAlternatif> call, Throwable t) {
                Log.d("Results", t.toString());
            }
        });
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
