package com.ksr.socialapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.ksr.socialapp.R;
import com.ksr.socialapp.tools.Methods;

import java.util.EmptyStackException;

public class LoginActivity extends BaseActivity{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private LinearLayout mainParentContainer;
    private EditText emailET,passwordET;
    private Button loginBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //hiding softkeyboard if user click out side of edittext
        mainParentContainer = findViewById(R.id.mainParentContainer);
        mainParentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideSoftKeyboard(getActivity());
            }
        });

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loginBT = findViewById(R.id.loginBT);

        //eye button functionality in passwordET
        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Methods.togglePasswordVisibility(getActivity(), motionEvent, passwordET);
            }
        });


        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailET.getText().toString().equals("")&&!passwordET.getText().toString().equals("")) {
                    attemptLogin(emailET.getText().toString(), passwordET.getText().toString());
                }else {
                    Toast.makeText(getActivity(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //navigating user to SignUp screen if wants to Register or doesn't have account
        findViewById(R.id.goToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    /*
        FirebaseAuth needs email and password to login, if data matches navigating user to Home screen
     */
    private void attemptLogin(String email,String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected BaseActivity getActivity() {
        return this;
    }
}