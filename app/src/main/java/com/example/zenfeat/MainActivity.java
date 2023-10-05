package com.example.zenfeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText password;
    Button btn;

    String emailString;
    String passwordString;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        TextView signup = findViewById(R.id.signup);

        signup.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });


        email = findViewById(R.id.email2);
        password = findViewById(R.id.password2);
        btn = findViewById(R.id.button);

        btn.setOnClickListener((v) -> {checkCredentials();});
    }

    private void checkCredentials(){

        passwordString = password.getText().toString();
        emailString = email.getText().toString();

        if (emailString.isEmpty()){
            Toast.makeText(getApplicationContext(),"first",Toast.LENGTH_LONG).show();
        } else if (passwordString.isEmpty()) {
            Toast.makeText(getApplicationContext(), "password", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, Home.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}