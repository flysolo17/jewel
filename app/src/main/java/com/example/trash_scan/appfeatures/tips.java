package com.example.trash_scan.appfeatures;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.trash_scan.adapter.ScheduleAdapter;


import com.example.trash_scan.databinding.ActivityTipsBinding;
import com.example.trash_scan.firebase.models.Schedule;
import com.example.trash_scan.firebase.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class tips extends Fragment {
    private ActivityTipsBinding binding;
    public ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;
    private FirebaseFirestore firestore;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = ActivityTipsBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        binding.recyclerviewSchedule.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
    private void getAllSchedules(String userAddress) {
        scheduleList = new ArrayList<>();
        firestore.collection(Schedule.TABLE_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    scheduleList.clear();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documents : task.getResult()) {
                            Schedule schedule = documents.toObject(Schedule.class);
                            if (schedule != null) {
                                for (String route: schedule.getRoutes()) {
                                    if (userAddress.contains(route)) {
                                        scheduleList.add(schedule);
                                    }
                                }
                            }
                        }
                        scheduleAdapter = new ScheduleAdapter(binding.getRoot().getContext(),scheduleList);
                        binding.recyclerviewSchedule.setAdapter(scheduleAdapter);
                    }
                });
            }
    private void getUserInfo(String userID) {
        firestore.collection(User.TABLE_NAME)
                .document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            getAllSchedules(user.getUserAddress());
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            getUserInfo(currentUser.getUid());
        }
    }
}