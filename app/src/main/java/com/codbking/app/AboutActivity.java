package com.codbking.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.support.v7.widget.AppCompatDrawableManager.get;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle("关于");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
