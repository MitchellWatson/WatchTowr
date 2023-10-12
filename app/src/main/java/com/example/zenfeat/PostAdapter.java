package com.example.zenfeat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private List<QueryDocumentSnapshot> data;

    private Bundle bundle;

    public PostAdapter(List<QueryDocumentSnapshot> data, Bundle bundleTemp) {
        this.data = data;
        this.bundle = bundleTemp;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        QueryDocumentSnapshot item = data.get(position);
        holder.bind(item.toObject(Post.class), item.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView companyTextView;
        private TextView positionTextView;
        private TextView reputationTextView;
        private TextView dateTextView;
        private LinearLayout expandedLayout;
        private Button buttonReputation;

        private TextView descriptionTextView;
        private TextView locationTextView;
        private TextView urlTextView;
        private TextView posterTextView;

        private FirebaseFirestore firestore;
        private String documentId; // Add this to store the Firestore document ID

        private boolean isExpanded = false;

        public MyViewHolder(View itemView) {
            super(itemView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            reputationTextView = itemView.findViewById(R.id.reputationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);
            buttonReputation = itemView.findViewById(R.id.buttonReputation);

            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
            posterTextView = itemView.findViewById(R.id.posterTextView);

            // Initialize Firestore database
            firestore = FirebaseFirestore.getInstance();

            buttonReputation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle reputation increment

                    DocumentReference cardRef = firestore.collection("posts").document(documentId);

                    cardRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Post card = documentSnapshot.toObject(Post.class);

                                // Check if the current user has already liked the card
                                if (!Arrays.asList(card.getUsersLiked()).contains(bundle.getString("uid"))) {
                                    // User hasn't liked the card, so update the database
                                    card.getUsersLiked().add(bundle.getString("uid")); // Add user to the "usersLiked" array
                                    card.setReputation(card.getReputation() + 1); // Increase reputation

                                    // Update the card document with the new data
                                    cardRef.set(card).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update the UI to reflect the new reputation count
                                        }
                                    });
                                } else {
                                    // User has already liked the card, provide feedback
                                }
                            }
                        }
                    });

                    int currentReputation = Integer.parseInt(reputationTextView.getText().toString());
                    int newReputation = currentReputation + 1;
                    reputationTextView.setText(String.valueOf(newReputation)); // Update the UI

                    // Update the Firestore document with the new reputation value
                    updateReputationInFirestore(newReputation);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isExpanded = !isExpanded;
                    expandedLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
        }

        public void bind(Post item, String documentIdTemp) {
            companyTextView.setText(item.getCompany());
            positionTextView.setText(item.getPosition());
            reputationTextView.setText(String.valueOf(item.getReputation()));
            dateTextView.setText(item.getDate().toString());

            descriptionTextView.setText(item.getDescription());
            locationTextView.setText(item.getLocation());
            urlTextView.setText(item.getUrl());
            posterTextView.setText(item.getPoster() + ", " + item.getOccupation());

            // Store the Firestore document ID
            documentId = documentIdTemp;
        }

        private void updateReputationInFirestore(int newReputation) {
            // Update the reputation in Firestore
            DocumentReference postRef = firestore.collection("posts").document(documentId);
            postRef.update("reputation", newReputation);
        }
    }

}