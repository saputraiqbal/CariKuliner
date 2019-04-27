package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRekomendasi {
    @SerializedName("result")
    @Expose
    private Rekomendasi rekomendasi;
    @SerializedName("msg")
    @Expose
    private Integer msg;

    public Rekomendasi getRekomendasi() {
        return rekomendasi;
    }

    public void setRekomendasi(Rekomendasi rekomendasi) {
        this.rekomendasi = rekomendasi;
    }

    public Integer getMsg() {
        return msg;
    }

    public void setMsg(Integer msg) {
        this.msg = msg;
    }
}
