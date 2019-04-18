package com.android.skripsi.carikuliner.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPilihData {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<PilihData> pilihDataList;
    @SerializedName("message")
    String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PilihData> getPilihDataList() {
        return pilihDataList;
    }

    public void setPilihDataList(List<PilihData> pilihDataList) {
        this.pilihDataList = pilihDataList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
