package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAlternatif {
    @SerializedName("result")
    @Expose
    private List<Alternatif> alternatifList;
    @SerializedName("msg")
    @Expose
    String msg;

    public List<Alternatif> getAlternatifList() {
        return alternatifList;
    }

    public void setAlternatifList(List<Alternatif> alternatifList) {
        this.alternatifList = alternatifList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
