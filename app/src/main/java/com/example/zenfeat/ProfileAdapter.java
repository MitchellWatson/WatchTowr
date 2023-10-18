package com.example.zenfeat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private List<QueryDocumentSnapshot> data;

    private Bundle bundle;

    private TextView popUp;

    public ProfileAdapter(List<QueryDocumentSnapshot> data, Bundle bundleTemp, TextView popUpTemp) {
        this.data = data;
        this.bundle = bundleTemp;
        this.popUp = popUpTemp;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post, parent, false);
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
        private TextView dateTextView;
        private Button deleteButton;
        private FirebaseFirestore firestore;
        private String documentId; // Add this to store the Firestore document ID

        public MyViewHolder(View itemView) {
            super(itemView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            deleteButton = itemView.findViewById(R.id.delete_button);

            // Initialize Firestore database
            firestore = FirebaseFirestore.getInstance();

            deleteButton.setOnClickListener((v) -> {
                DocumentReference cardRef = firestore.collection("posts").document(documentId);

                cardRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                popUp.setText("Delete success");

                                int position = getAdapterPosition();
                                data.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, data.size());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                popUp.setText("Delete unsuccessful");
                            }
                        });
                });
        }

        public void bind(Post item, String documentIdTemp) {
            companyTextView.setText(item.getCompany());
            positionTextView.setText(item.getPosition());

            Timestamp newTimestamp = Timestamp.now();

            long secondsTemp = newTimestamp.getSeconds() - item.getDate().getSeconds();

            String formattedDuration = formatDuration(secondsTemp);

            dateTextView.setText(formattedDuration);

            // Store the Firestore document ID
            documentId = documentIdTemp;
        }

        public String formatDuration(long seconds) {
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                return days + "d ago";
            } else if (hours > 0) {
                return hours + "h ago";
            } else if (minutes > 0) {
                return minutes + "min ago";
            } else {
                return seconds + "s ago";
            }
        }
    }

}