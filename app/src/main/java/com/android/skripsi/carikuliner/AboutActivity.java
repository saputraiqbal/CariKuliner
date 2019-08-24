package com.android.skripsi.carikuliner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About Apps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imgMe = findViewById(R.id.img_me);
        ImageView imgApp = findViewById(R.id.imgIcon);
        TextView appName = findViewById(R.id.tv_nameApp);
        TextView appVer = findViewById(R.id.tv_verApp);

        Glide.with(this)
                .load(R.drawable.ic_launcher_foreground)
                .into(imgApp);
        appName.setText(R.string.app_name);
        appVer.setText("ver. " + BuildConfig.VERSION_NAME + ", 2019 August");
        Glide.with(this)
                .load("https://avatars1.githubusercontent.com/u/17638445?s=460&v=4")
                .into(imgMe);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
