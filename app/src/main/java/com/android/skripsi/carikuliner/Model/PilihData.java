package com.android.skripsi.carikuliner.Model;

import com.google.gson.annotations.SerializedName;

public class PilihData {
    @SerializedName("id")
    private String idData;
    @SerializedName("namaTempat")
    private String namaTempat;
    @SerializedName("alamat")
    private String alamat;

    public String getIdData() {
        return idData;
    }

    public void setIdData(String idData) {
        this.idData = idData;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
