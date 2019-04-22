package com.android.skripsi.carikuliner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alternatif {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("namaTempat")
    @Expose
    private String namaTempat;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
