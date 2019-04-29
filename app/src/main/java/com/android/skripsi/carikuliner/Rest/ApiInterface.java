package com.android.skripsi.carikuliner.rest;

import com.android.skripsi.carikuliner.model.GetAlternatif;
import com.android.skripsi.carikuliner.model.GetRekomendasi;
import com.android.skripsi.carikuliner.model.Rekomendasi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("get_alldata")
    Call<GetAlternatif> getDataAll();

    @GET("recommendations")
    Call<GetRekomendasi> getRekomendasi(@Query("lat") String lat,
                                           @Query("long") String _long,
                                           @Query("weight") String weight,
                                           @Query("choice") String choice);
}
