package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDetail {
    @SerializedName("result")
    @Expose
    private DetailInfo detailInfo;
    @SerializedName("msg")
    @Expose
    String msg;

    public DetailInfo getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(DetailInfo detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
