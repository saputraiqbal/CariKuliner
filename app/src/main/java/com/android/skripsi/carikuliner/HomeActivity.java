package com.android.skripsi.carikuliner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    Button beginFind;
    TextView txtViewJarak, txtViewRating, txtViewHarga, txtViewUsia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupUI();
    }

    private void setupUI(){
        txtViewJarak = findViewById(R.id.txtIntroJarak);
        txtViewHarga = findViewById(R.id.txtIntroHarga);
        txtViewRating = findViewById(R.id.txtIntroRating);
        txtViewUsia = findViewById(R.id.txtIntroUmur);
        beginFind = findViewById(R.id.btnBegin);

        txtViewJarak.setText(R.string.intro_jarak);
        txtViewHarga.setText(R.string.intro_harga);
        txtViewRating.setText(R.string.intro_rating);
        txtViewUsia.setText(R.string.intro_usia);
        beginFind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBegin:
                Intent toChooseAlt = new Intent(HomeActivity.this, PilihKategoriActivity.class);
                startActivityForResult(toChooseAlt, 1);
                break;
        }
    }
}
