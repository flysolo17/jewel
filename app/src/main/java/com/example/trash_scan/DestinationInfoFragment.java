package com.example.trash_scan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.trash_scan.databinding.FragmentDestinationInfoBinding;
import com.example.trash_scan.firebase.models.Collector;
import com.example.trash_scan.firebase.models.Destinations;
import com.example.trash_scan.viewmodels.DestinationViewModel;
import com.google.firebase.firestore.FirebaseFirestore;



public class DestinationInfoFragment extends Fragment {

    private DestinationViewModel destinationViewModel;
    private FirebaseFirestore firestore;
    private FragmentDestinationInfoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDestinationInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        destinationViewModel =new  ViewModelProvider(requireActivity()).get(DestinationViewModel.class);
        destinationViewModel.getDestination().observe(getViewLifecycleOwner(), destinations -> {
            displayDestinationInfo(destinations);
        });
    }

    private void displayDestinationInfo(Destinations destinations) {

        if (destinations != null) {
            getCollectorInfo(destinations.getCollectorID());
            binding.textInfo.setText(" is now collectiong in " + destinations.getListAddresses().get(destinations.getNowCollecting()));

            binding.stepView
                    .setStepsViewIndicatorComplectingPosition(destinations.getListAddresses().size())
                    .reverseDraw(false)

                    .setStepViewTexts(destinations.getListAddresses())//总步骤
                    .setLinePaddingProportion(0.85f)
                    .setTextSize(12)//set textSize
                    .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), android.R.color.white))//设置StepsViewIndicator完成线的颜色
                    .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                    .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), android.R.color.white))//设置StepsView text完成线的颜色
                    .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                    .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                    .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                    .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention));
            binding.stepView.setStepsViewIndicatorComplectingPosition(destinations.getNowCollecting());
        }
    }

    private void getCollectorInfo(String collectorID) {
        firestore.collection(Collector.TABLE_NAME)
                .document(collectorID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Collector collector = documentSnapshot.toObject(Collector.class);
                        binding.textCollectorName.setText(collector.getFirstName() + " " + collector.getLastName());
                        binding.textPlateNumber.setText(collector.getPlateNumber());
                    }
                });
    }
}