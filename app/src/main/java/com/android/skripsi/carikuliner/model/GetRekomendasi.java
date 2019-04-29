package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRekomendasi {
    @SerializedName("result")
    @Expose
    private Rekomendasi result;
    @SerializedName("msg")
    @Expose
    private int msg;

    public Rekomendasi getResult() {
        return result;
    }

    public void setResult(Rekomendasi result) {
        this.result = result;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
