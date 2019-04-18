package com.android.skripsi.carikuliner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.skripsi.carikuliner.Model.PilihData;
import com.android.skripsi.carikuliner.Model.GetPilihData;
import com.android.skripsi.carikuliner.Rest.ApiClient;
import com.android.skripsi.carikuliner.Rest.ApiInterface;
import com.android.skripsi.carikuliner.Adapter.AdapterData;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_data);

        rvData = (RecyclerView) findViewById(R.id.rvData);
        rvManager = new LinearLayoutManager(this);
        rvData.setLayoutManager(rvManager);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pdAct = this;
        refresh();
    }

    public void refresh(){
        Call<GetPilihData> pilihDataCall = apiInterface.getPilihDataCall();
        pilihDataCall.enqueue(new Callback<GetPilihData>() {
            @Override
            public void onResponse(Call<GetPilihData> call, Response<GetPilihData> response) {
                List<PilihData> pilihDataList = response.body().getPilihDataList();
                Log.d("Retrofit Get", "Jumlah data : " + String.valueOf(pilihDataList.size()));
                adapterData = new AdapterData(pilihDataList);
                rvData.setAdapter(adapterData);
            }

            @Override
            public void onFailure(Call<GetPilihData> call, Throwable t) {
                Log.d("Retrofit Get", t.toString());
            }
        });
    }
}
