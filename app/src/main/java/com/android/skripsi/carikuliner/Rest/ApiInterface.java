package com.android.skripsi.carikuliner.rest;

import com.android.skripsi.carikuliner.model.GetKategori;
import com.android.skripsi.carikuliner.model.GetDetail;
import com.android.skripsi.carikuliner.model.GetRekomendasi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("get_category")
    Call<GetKategori> getKategori();

    @GET("recommendations")
    Call<GetRekomendasi> getRekomendasi(@Query("lat") double lat,
                                        @Query("long") double _long,
                                        @Query("cat") String category,
                                        @Query("weight") String weight);

    @GET("get_datarecommend")
    Call<GetDetail> getDetail(@Query("id") String id);
}
