package com.example.zenfeat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private User user;

    public ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        Button signout = (Button) rootview.findViewById(R.id.signout);

        signout.setOnClickListener((v) -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            mAuth.signOut();

            startActivity(new Intent(requireContext(), MainActivity.class));
        });

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize the RecyclerView
        RecyclerView recyclerView = rootview.findViewById(R.id.jobAlertsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        TextView popUp = rootview.findViewById(R.id.popUp);

        List<QueryDocumentSnapshot> data = new ArrayList<>();

        Home home = (Home)getActivity();

        ProfileAdapter adapter = new ProfileAdapter(data, home.bundle, popUp);
        recyclerView.setAdapter(adapter);

        Query query = db.collection("posts")
                .whereEqualTo("uid", home.bundle.getString("uid"));

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            data.add(document);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure, e.g., show an error message
                        Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
                        Log.e("profile", ""+e);
                    }
                });

        Button save = rootview.findViewById(R.id.save);
         save.setOnClickListener((v) -> {

             TextView occupation = rootview.findViewById(R.id.occupationEdit);
             TextView age = rootview.findViewById(R.id.ageEdit);

             DocumentReference userRef = db.collection("users").document(home.bundle.getString("uid"));

             if (!occupation.getText().toString().isEmpty() && !age.getText().toString().isEmpty()){

                 userRef.get()
                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 if (documentSnapshot.exists()) {
                                     user = documentSnapshot.toObject(User.class);

                                     user.setOccupation(occupation.getText().toString());
                                     user.setAge(age.getText().toString());

                                     userRef.set(user)
                                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     popUp.setTextColor(getResources().getColor(R.color.success, null));
                                                     popUp.setText("User info update successful");
                                                     InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                     imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                                                 }
                                             })
                                             .addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                     popUp.setTextColor(getResources().getColor(R.color.error, null));
                                                     popUp.setText("User info update unsuccessful");
                                                     InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                     imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                                                 }
                                             });
                                 }
                             }
                         });

             } else {

                 popUp.setTextColor(getResources().getColor(R.color.error, null));
                 popUp.setText("Please add both occupation and age");

             }
         });

        return rootview;
    }
}