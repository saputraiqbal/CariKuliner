package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailInfo {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("namaTempat")
    @Expose
    private String namaTempat;
    private double jarak;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("tahunBerdiri")
    @Expose
    private String tahunBerdiri;

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTahunBerdiri() {
        return tahunBerdiri;
    }

    public void setTahunBerdiri(String tahunBerdiri) {
        this.tahunBerdiri = tahunBerdiri;
    }
}
