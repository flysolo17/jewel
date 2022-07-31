package com.ketchupzzz.gso.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.ScheduleAdapter
import com.ketchupzzz.gso.databinding.FragmentAddScheduleBinding
import com.ketchupzzz.gso.databinding.FragmentScheduleBinding
import com.ketchupzzz.gso.model.Schedule


class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var scheduleAdapter : ScheduleAdapter
    private lateinit var scheduleList : MutableList<Schedule>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun init() {
        binding.recyclerviewScheds.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getAllSchedules(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    private fun getAllSchedules(myID : String) {
        scheduleList = mutableListOf()
        firestore.collection(Schedule.TABLE_NAME)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    error.printStackTrace()
                } else {
                    value?.map { documents ->
                        val schedule = documents.toObject(Schedule::class.java)
                        if (schedule.gsoID.equals(myID)) {
                            scheduleList.add(schedule)
                        }
                    }
                    scheduleAdapter = ScheduleAdapter(binding.root.context,scheduleList)
                    binding.recyclerviewScheds.adapter = scheduleAdapter
                }
            }
    }
}