package com.example.trash_scan.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trash_scan.R;
import com.example.trash_scan.firebase.models.Collector;
import com.example.trash_scan.firebase.models.Destinations;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DestinationAdapter extends FirestoreRecyclerAdapter<Destinations,DestinationAdapter.DestinationAdapterViewHolder> {
    public interface OnDestinationClick {
        void onDestinationClicked(int position);
    }
    OnDestinationClick onDestinationClick;
    public DestinationAdapter(@NonNull FirestoreRecyclerOptions<Destinations> options,OnDestinationClick onDestinationClick) {
        super(options);
        this.onDestinationClick = onDestinationClick;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull DestinationAdapterViewHolder holder, int position, @NonNull Destinations model) {
        holder.getCollectorInfo(model.getCollectorID(),model.getListAddresses().get(model.getNowCollecting()));
        holder.itemView.setOnClickListener(v -> {
            onDestinationClick.onDestinationClicked(position);
        });
    }
    @NonNull
    @Override
    public DestinationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_destinations,parent,false);
        return new DestinationAdapterViewHolder(view);
    }

    public static class DestinationAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textNote;

        public DestinationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textNote = itemView.findViewById(R.id.textNote);

        }

        public void getCollectorInfo(String collectorID,String destination) {
            FirebaseFirestore.getInstance()
                    .collection("Collector")
                    .document(collectorID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Collector collector = documentSnapshot.toObject(Collector.class);
                            textNote.setText(collector.getFirstName() + " " + collector.getFirstName()
                            + " is now collectiong in " + destination);
                        }
                    });
        }

    }


}
