package com.android.skripsi.carikuliner.rest;

import com.android.skripsi.carikuliner.model.GetAlternatif;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("get_alldata")
    Call<GetAlternatif> getDataAll();
}
