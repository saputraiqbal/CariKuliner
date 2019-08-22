package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetRekomendasi {
    @SerializedName("result")
    @Expose
    private ArrayList<Rekomendasi> result;
    @SerializedName("msg")
    @Expose
    private int msg;

    public GetRekomendasi(ArrayList<Rekomendasi> result) {
        this.result = result;
    }

    public ArrayList<Rekomendasi> getResult() {
        return result;
    }

    public void setResult(ArrayList<Rekomendasi> result) {
        this.result = result;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
