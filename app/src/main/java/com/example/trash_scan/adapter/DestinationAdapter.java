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
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public DestinationAdapter(@NonNull FirestoreRecyclerOptions<Destinations> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull DestinationAdapterViewHolder holder, int position, @NonNull Destinations model) {
        holder.textNote.setText(model.getListAddresses().get(model.getNowCollecting()));
        holder.getCollectorInfo(model.getCollectorID());
    }
    @NonNull
    @Override
    public DestinationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_destinations,parent,false);
        return new DestinationAdapterViewHolder(view);
    }

    public static class DestinationAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textNote;
        TextView collectorName;
        public DestinationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textNote = itemView.findViewById(R.id.textNote);
            collectorName = itemView.findViewById(R.id.textCollectorName);
        }

        public void getCollectorInfo(String collectorID) {

            FirebaseFirestore.getInstance()
                    .collection("Collector")
                    .document(collectorID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Collector collector = documentSnapshot.toObject(Collector.class);
                            collectorName.setText(collector.getFirstName() + " " + collector.getLastName());

                        }
                    });


        }

    }


}
