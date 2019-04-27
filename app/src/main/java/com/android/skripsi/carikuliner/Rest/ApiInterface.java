package com.android.skripsi.carikuliner.rest;

import com.android.skripsi.carikuliner.model.GetAlternatif;
import com.android.skripsi.carikuliner.model.GetRekomendasi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("get_alldata")
    Call<GetAlternatif> getDataAll();

    @GET("recommendations")
    Call<GetRekomendasi> getRekomendasi(@Query("lat") String latUser,
                                        @Query("long") String longUser,
                                        @Query("weight") String weightUser,
                                        @Query("choise") String choice);
}
