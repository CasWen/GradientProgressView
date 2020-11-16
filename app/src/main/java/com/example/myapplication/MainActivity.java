package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GradientProgressView my_progress = findViewById(R.id.my_progress);
        my_progress.setMaxCount(100);
        my_progress.setProgress(66);
    }
}