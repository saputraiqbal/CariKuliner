package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rekomendasi {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("namaTempat")
    @Expose
    private String namaTempat;
    @SerializedName("jarak")
    @Expose
    private double jarak;
    @SerializedName("latTempat")
    @Expose
    private String latTempat;
    @SerializedName("lonTempat")
    @Expose
    private String lonTempat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }

    public String getLatTempat() {
        return latTempat;
    }

    public void setLatTempat(String latTempat) {
        this.latTempat = latTempat;
    }

    public String getLonTempat() {
        return lonTempat;
    }

    public void setLonTempat(String lonTempat) {
        this.lonTempat = lonTempat;
    }
}
