package com.ketchupzzz.gso.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzzz.gso.model.Gso

class GsoViewModel : ViewModel() {
    val selected = MutableLiveData<Gso>()
    fun setGso(gso: Gso) {
        selected.value = gso
    }
    fun getGso() : LiveData<Gso> {
        return selected
    }
}