package com.flysolo.collectorapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdressViewModel : ViewModel() {
    val selected = MutableLiveData<Int>()

    fun select(address: Int) {
        selected.value = address
    }
    fun getSelected() : LiveData<Int> {
        return selected
    }
}