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

        TextView error = (TextView) findViewById(R.id.errorView);

        if (firstname.isEmpty()){
            error.setText("Please fill in First Name");
        } else if (lastname.isEmpty()) {
            error.setText("Please fill in Last Name");
        } else if (passwordString.isEmpty() || confirmString.isEmpty() || passwordString.compareTo(confirmString) !=0){
            error.setText("Passwords must match");
        } else if (passwordString.length() < 8){
            error.setText("Password must be at least 8 characters");
        } else if (emailString.isEmpty()) {
            error.setText("Please fill in Email");
        }  else if (ageString.isEmpty()){
            error.setText("Please fill in Age");
        } else if (occupationString.isEmpty()) {
            error.setText("Please fill in Occupation");
        } else {
            mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();
                                User newUser = new User(userId, firstname, lastname, ageString, occupationString, emailString);
                                db.collection("users").document(userId)
                                        .set(newUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent intent = new Intent(SignUp.this, Home.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("first", firstname);
                                                bundle.putString("last", lastname);
                                                bundle.putString("age", ageString);
                                                bundle.putString("occupation", occupationString);
                                                bundle.putString("email", emailString);
                                                bundle.putString("uid", userId);
                                                intent.putExtras(bundle);

                                                startActivity(intent);
                                            }
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                error.setText("Email already in use");
                            }
                        }
                    });        }
    }
}