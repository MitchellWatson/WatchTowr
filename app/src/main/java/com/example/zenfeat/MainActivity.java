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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference userRef = db.collection("users").document(userId);

                        Bundle bundle = new Bundle();

                        userRef.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            // The document exists; you can access its data
                                            User user = documentSnapshot.toObject(User.class);
                                                // Access user properties
                                            bundle.putString("first", user.getFirstName());
                                            bundle.putString("last", user.getLastName());
                                            bundle.putString("age", user.getAge());
                                            bundle.putString("occupation", user.getOccupation());
                                            bundle.putString("email", user.getEmail());
                                            bundle.putString("uid", userId);
                                            startActivity(new Intent(MainActivity.this, Home.class).putExtras(bundle));

                                        }
                                    }
                                });


                    } else {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}