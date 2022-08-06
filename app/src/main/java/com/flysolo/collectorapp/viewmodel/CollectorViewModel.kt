package com.flysolo.collectorapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flysolo.collectorapp.models.Collector
import okhttp3.Cookie

class CollectorViewModel : ViewModel() {
    val selected = MutableLiveData<Collector>()
    fun setCollector(collector: Collector) {
        selected.value = collector
    }
    fun getCollector() : LiveData<Collector> {
        return selected
    }
}