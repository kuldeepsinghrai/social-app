package com.ksr.socialapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ksr.socialapp.R;

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startSplashScreen();

    }

    private void startSplashScreen() {
        handler.postDelayed(runnable, 2000);
    }


    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    };


}