package com.android.skripsi.carikuliner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BobotActivity extends AppCompatActivity implements View.OnClickListener{
    Button add1, subs1, add2, subs2, add3, subs3, add4, subs4, confirm;
    TextView boxJarak, boxHarga, boxRating, boxUsia;
    int jarak = 1, harga = 1, rating = 1, usia = 1;
    String result = "";
    SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Penilaian Kriteria");
        setupUI();
    }

    private void setupUI(){
        add1 = findViewById(R.id.btnAddBobot1);
        subs1 = findViewById(R.id.btnSubsBobot1);
        add2 = findViewById(R.id.btnAddBobot2);
        subs2 = findViewById(R.id.btnSubsBobot2);
        add3 = findViewById(R.id.btnAddBobot3);
        subs3 = findViewById(R.id.btnSubsBobot3);
        add4 = findViewById(R.id.btnAddBobot4);
        subs4 = findViewById(R.id.btnSubsBobot4);
        confirm = findViewById(R.id.btnOK1);
        boxJarak = findViewById(R.id.boxBobot1);
        boxHarga = findViewById(R.id.boxBobot2);
        boxRating = findViewById(R.id.boxBobot3);
        boxUsia = findViewById(R.id.boxBobot4);

        boxJarak.setText(String.valueOf(jarak));
        boxHarga.setText(String.valueOf(harga));
        boxRating.setText(String.valueOf(rating));
        boxUsia.setText(String.valueOf(usia));

        add1.setOnClickListener(this);
        subs1.setOnClickListener(this);
        add2.setOnClickListener(this);
        subs2.setOnClickListener(this);
        add3.setOnClickListener(this);
        subs3.setOnClickListener(this);
        add4.setOnClickListener(this);
        subs4.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddBobot1:
                jarak = Integer.parseInt(boxJarak.getText().toString());
                if (jarak < 5){
                    jarak++;
                    boxJarak.setText(String.valueOf(jarak));
                }
                break;
            case R.id.btnAddBobot2:
                harga = Integer.parseInt(boxHarga.getText().toString());
                if (harga < 5){
                    harga++;
                    boxHarga.setText(String.valueOf(harga));
                }
                break;
            case R.id.btnAddBobot3:
                rating = Integer.parseInt(boxRating.getText().toString());
                if (rating < 5){
                    rating++;
                    boxRating.setText(String.valueOf(rating));
                }
                break;
            case R.id.btnAddBobot4:
                usia = Integer.parseInt(boxUsia.getText().toString());
                if (usia < 5){
                    usia++;
                    boxUsia.setText(String.valueOf(usia));
                }
                break;
            case R.id.btnSubsBobot1:
                jarak = Integer.parseInt(boxJarak.getText().toString());
                if (jarak > 1){
                    jarak--;
                    boxJarak.setText(String.valueOf(jarak));
                }
                break;
            case R.id.btnSubsBobot2:
                harga = Integer.parseInt(boxHarga.getText().toString());
                if (harga > 1){
                    harga--;
                    boxHarga.setText(String.valueOf(harga));
                }
                break;
            case R.id.btnSubsBobot3:
                rating = Integer.parseInt(boxRating.getText().toString());
                if (rating > 1){
                    rating--;
                    boxRating.setText(String.valueOf(rating));
                }
                break;
            case R.id.btnSubsBobot4:
                usia = Integer.parseInt(boxUsia.getText().toString());
                if (usia > 1){
                    usia--;
                    boxUsia.setText(String.valueOf(usia));
                }
                break;
            case R.id.btnOK1:
                result = boxJarak.getText().toString() + "-" + boxHarga.getText().toString() + "-" + boxRating.getText().toString()  + "-" + boxUsia.getText().toString();
                SharedPreferences shares = getSharedPreferences("value_stores", MODE_PRIVATE);
                SharedPreferences.Editor editor = shares.edit();
                editor.putString("weight", result);
                editor.commit();
                Log.d("itemStored", "[" + shares.getString("weight", result) + "] was added to SharedPreferences");
                Intent toChooseAlt = new Intent(BobotActivity.this, PilihDataActivity.class);
                startActivityForResult(toChooseAlt, 1);
                break;
        }
    }
}
