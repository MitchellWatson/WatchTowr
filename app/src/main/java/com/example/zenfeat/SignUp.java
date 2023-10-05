package com.example.zenfeat;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class SignUp extends AppCompatActivity {

    Button btn;
    TextInputEditText first;
    TextInputEditText last;
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText confirm;

    TextInputEditText age;
    TextInputEditText occupation;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn = findViewById(R.id.button);
        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        age = findViewById(R.id.age);
        occupation = findViewById(R.id.occupation);
        TextView login = findViewById(R.id.login);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        login.setOnClickListener((v) -> {
            startActivity(new Intent(SignUp.this, MainActivity.class));
        });

        btn.setOnClickListener((v) -> { checkCredentials(); });

    }

    private void checkCredentials() {
        String firstname = first.getText().toString();
        String lastname = last.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String confirmString = confirm.getText().toString();
        String ageString = age.getText().toString();
        String occupationString = occupation.getText().toString();

        if (firstname.isEmpty()){
            Toast.makeText(getApplicationContext(),"first",Toast.LENGTH_LONG).show();
        } else if (lastname.isEmpty()) {
            Toast.makeText(getApplicationContext(),"long",Toast.LENGTH_LONG).show();
        } else if (passwordString.isEmpty() || confirmString.isEmpty() || passwordString.compareTo(confirmString) !=0){
            Toast.makeText(getApplicationContext(),"password",Toast.LENGTH_LONG).show();
        } else if (emailString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"email",Toast.LENGTH_LONG).show();
        }  else if (ageString.isEmpty()){
            Toast.makeText(getApplicationContext(),"age",Toast.LENGTH_LONG).show();
        } else if (occupationString.isEmpty()) {
            Toast.makeText(getApplicationContext(),"occupation",Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
//                                Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();
                                User newUser = new User(userId, firstname, lastname, ageString, occupationString, emailString);
                                db.collection("users").document(userId)
                                        .set(newUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
                            }
                        }
                    });        }
    }
}