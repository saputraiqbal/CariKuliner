package com.android.skripsi.carikuliner.Rest;

import com.android.skripsi.carikuliner.Model.GetPilihData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

public interface ApiInterface {
    @GET("get_datarecommend")
    Call<GetPilihData> getPilihDataCall();
}
