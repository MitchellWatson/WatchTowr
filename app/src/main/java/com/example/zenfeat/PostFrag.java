package com.example.zenfeat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFrag newInstance(String param1, String param2) {
        PostFrag fragment = new PostFrag();
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
        View rootview = inflater.inflate(R.layout.fragment_post, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Home home = (Home)getActivity();
        String poster = home.bundle.getString("first") + " " + home.bundle.getString("last");

        rootview.findViewById(R.id.submit).setOnClickListener((v) -> {

            String company = ((TextInputEditText)rootview.findViewById(R.id.company)).getText().toString();
            String position = ((TextInputEditText)rootview.findViewById(R.id.position)).getText().toString();
            String url = ((TextInputEditText)rootview.findViewById(R.id.url)).getText().toString();
            String description = ((TextInputEditText)rootview.findViewById(R.id.description)).getText().toString();
            String location = ((TextInputEditText)rootview.findViewById(R.id.location)).getText().toString();
            List<String> usersLiked = new ArrayList<String>();
            TextView popUp = rootview.findViewById(R.id.postPopUp);
            db.collection("posts")
                    .add(new Post(company, description, position, location, url, 0, poster, home.bundle.getString("occupation"), userId, Timestamp.now(), usersLiked))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Document successfully added
                            popUp.setTextColor(getResources().getColor(R.color.success, null));
                            popUp.setText("Post successfully created");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            popUp.setTextColor(getResources().getColor(R.color.error, null));
                            popUp.setText("Post creation unsuccessful");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    });
        });



        return rootview;
    }
}