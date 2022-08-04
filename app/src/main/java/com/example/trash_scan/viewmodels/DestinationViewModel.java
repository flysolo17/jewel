package com.example.trash_scan.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trash_scan.firebase.models.Destinations;

public class DestinationViewModel extends ViewModel {
    MutableLiveData<Destinations> selected = new MutableLiveData<>();

    public void setDestination(Destinations destination){
        selected.setValue(destination);
    }
    public LiveData<Destinations> getDestination() {
        return selected;
    }
}
