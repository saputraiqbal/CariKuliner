package com.android.skripsi.carikuliner.rest;

import com.android.skripsi.carikuliner.model.GetKategori;
import com.android.skripsi.carikuliner.model.GetDetail;
import com.android.skripsi.carikuliner.model.GetRekomendasi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//interface used to achieve some data from server using RESTful web service
public interface ApiInterface {
    //get category data from server
    @GET("get_category")
    Call<GetKategori> getKategori();

    //get recommendation data from server
    @GET("recommendations")
    Call<GetRekomendasi> getRekomendasi(@Query("lat") double lat,
                                        @Query("long") double _long,
                                        @Query("cat") String category,
                                        @Query("weight") String weight);

    //get detail of recommendation place chosen by users
    @GET("get_datarecommend")
    Call<GetDetail> getDetail(@Query("id") String id);
}
