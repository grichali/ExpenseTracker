package com.example.exspenses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide(); // Hide the action bar

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Navigate to Dashboard
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity
        }, 3000); // 3 sec delay
    }

}