package com.flysolo.collectorapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdressViewModel : ViewModel() {
    private val selected = MutableLiveData<Int>()
    private val selectedAddress = MutableLiveData<String>()
    fun select(address: Int) {
        selected.value = address
    }
    fun getSelected() : LiveData<Int> {
        return selected
    }
    fun setAddress(address: String) {
        selectedAddress.value = address
    }
    fun getAddress() : LiveData<String> {
        return selectedAddress
    }

}