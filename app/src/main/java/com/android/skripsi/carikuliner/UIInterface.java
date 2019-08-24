package com.android.skripsi.carikuliner;

import android.support.annotation.NonNull;

import com.android.skripsi.carikuliner.model.Rekomendasi;

public interface UIInterface {
    void setupUI(); //to build the UI
    void loadData(); //to achieve data from server
    void updateUI(); // to apply data received to the UI
}
