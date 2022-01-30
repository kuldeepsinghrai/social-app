package com.ksr.socialapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.ksr.socialapp.R;

import java.util.EmptyStackException;

public class LoginActivity extends BaseActivity{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseCurrentUser;

    private EditText emailET,passwordET;
    private Button loginBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseCurrentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loginBT = findViewById(R.id.loginBT);

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(emailET.getText().toString(),passwordET.getText().toString());
            }
        });


        findViewById(R.id.goToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void attemptLogin(String email,String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(getActivity(), "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseCurrentUser!=null){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }

    @Override
    protected BaseActivity getActivity() {
        return this;
    }
}