package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rekomendasi {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String namaTempat;
    @SerializedName("jarak")
    @Expose
    private int jarak;
    @SerializedName("lat")
    @Expose
    private float latitude;
    @SerializedName("long")
    @Expose
    private float longitude;

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

    public int getJarak() {
        return jarak;
    }

    public void setJarak(int jarak) {
        this.jarak = jarak;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
