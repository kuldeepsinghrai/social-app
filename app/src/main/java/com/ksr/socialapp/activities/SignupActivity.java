package com.ksr.socialapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;
import com.ksr.socialapp.R;
import com.ksr.socialapp.model.User;
import com.ksr.socialapp.tools.Methods;

public class SignupActivity extends BaseActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private LinearLayout mainParentContainer;
    private EditText nameET, professionET, emailET, passwordET;
    private Button signUpBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mainParentContainer = findViewById(R.id.mainParentContainer);
        mainParentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideSoftKeyboard(getActivity());
            }
        });

        nameET = findViewById(R.id.nameET);
        professionET = findViewById(R.id.professionET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signUpBT = findViewById(R.id.signUpBT);


        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Methods.togglePasswordVisibility(getActivity(), motionEvent, passwordET);
            }
        });


        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameET.getText().toString().equals("") && !professionET.getText().toString().equals("") && !emailET.getText().toString().equals("") && !passwordET.getText().toString().equals("")) {
                    attemptSignUp(nameET.getText().toString(), professionET.getText().toString(), emailET.getText().toString(), passwordET.getText().toString());
                }else {
                    Toast.makeText(getActivity(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        findViewById(R.id.goToLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void attemptSignUp(String name, String profession, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(name, profession, email, password);
                    String id = task.getResult().getUser().getUid();
                    firebaseDatabase.getReference().child("Users").child(id).setValue(user);
                    Toast.makeText(getActivity(), "User Data Saved!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getActivity(), "Wrong User Details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected BaseActivity getActivity() {
        return this;
    }
}