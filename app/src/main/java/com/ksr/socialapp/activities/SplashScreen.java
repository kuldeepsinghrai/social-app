package com.ksr.socialapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.ksr.socialapp.R;

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseCurrentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
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
            if (firebaseCurrentUser != null) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            } else {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    };


}