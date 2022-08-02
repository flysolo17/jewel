package com.ketchupzzz.gso.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzzz.gso.model.Schedule

class ScheduleViewModel : ViewModel() {
    private val selected  = MutableLiveData<Schedule>()

    fun setSchedule(schedule: Schedule) {
        selected.value = schedule
    }
    fun getSchedule() : LiveData<Schedule> {
        return selected
    }
}