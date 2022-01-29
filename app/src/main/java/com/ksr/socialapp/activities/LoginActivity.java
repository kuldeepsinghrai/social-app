package com.ksr.socialapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ksr.socialapp.R;

import java.util.EmptyStackException;

public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.goToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected BaseActivity getActivity() {
        return this;
    }
}