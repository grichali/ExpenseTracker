package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide(); // Hide the action bar

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Navigate to Dashboard
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity
        }, 3000); // 3 sec delay
    }

}
