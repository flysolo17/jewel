package com.example.trash_scan.fragments;

import android.media.MediaRouter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trash_scan.R;

import com.example.trash_scan.databinding.FragmentMapsBinding;
import com.example.trash_scan.firebase.models.LatLang;
import com.example.trash_scan.firebase.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FragmentMapsBinding binding;
    private String address = null;
    private List<LatLng> latLangList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        for (LatLng latLng: latLangList) {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        if (!latLangList.isEmpty()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLangList.get(latLangList.size() - 1), 15f));
        }
    }
    private void getLatLang(String address) {
        latLangList.clear();
        FirebaseFirestore.getInstance().collection("1")
                .get()
                .addOnCompleteListener(task -> {
                    latLangList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (LatLang latLang: task.getResult().toObjects(LatLang.class)) {
                            if (address.contains(latLang.getAddress())) {
                                LatLng latLng = new LatLng(Long.parseLong(latLang.getLatitude()),Long.parseLong(latLang.getLongitude()));
                                latLangList.add(latLng);
                            }
                        }
                    }
                });
    }
    private void getUserInfo(String userId){
        FirebaseFirestore.getInstance().collection(User.TABLE_NAME)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            address = user.getUserAddress();
                            getLatLang(address);
                        }
                    }
            });
        }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUserInfo(user.getUid());
        }
    }
}