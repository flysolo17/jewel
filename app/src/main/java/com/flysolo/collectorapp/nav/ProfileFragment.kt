package com.flysolo.collectorapp.nav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.adapters.ScheduleAdapter
import com.flysolo.collectorapp.databinding.FragmentHomeBinding
import com.flysolo.collectorapp.databinding.FragmentProfileBinding
import com.flysolo.collectorapp.login.LoginActivity
import com.flysolo.collectorapp.models.Collector
import com.flysolo.collectorapp.models.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var collector : Collector? = null
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var scheduleList : MutableList<Schedule>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewSchedule.layoutManager = LinearLayoutManager(view.context)
        getAllSchedules(FirebaseAuth.getInstance().currentUser?.uid)
        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity,LoginActivity::class.java))
        }
        binding.updateProfile.setOnClickListener {
            if (collector != null) {
                val updateProfile = UpdateProfile.newInstance(
                    param1 = collector!!.id!!,
                    param2 = collector!!.firstName!!,
                    param3 = collector!!.lastName!!,
                    param4 = collector!!.phone!!,
                    param5 = collector!!.plateNumber!!,
                    param6 = collector!!.email!!)
                if (!updateProfile.isAdded) {
                    updateProfile.show(childFragmentManager,"Update Profile")
                }
            }

        }
    }

    private fun getCollectorInfo(collectorID : String) {
        firestore.collection(Collector.TABLE_NAME)
            .document(collectorID)
            .get()
            .addOnSuccessListener {document ->
                if (document.exists()) {
                    val collector = document.toObject(Collector::class.java)
                    this.collector = collector
                    displayCollectorInfo(collector)
                }
            }
    }

    private fun displayCollectorInfo(collector: Collector?) {
        if (collector != null) {
            binding.textFullname.text = "${collector.firstName} ${collector.lastName}"
            binding.textEmail.text = collector.email
            binding.textPhoneNumber.text  = collector.phone
            binding.textPlateNumber.text = collector.plateNumber
        }
    }
    private fun getAllSchedules(myID : String?) {
        scheduleList = mutableListOf()
        firestore.collection(Schedule.TABLE_NAME)
            .whereEqualTo(Schedule.COLLECTOR_ID,myID)
            .addSnapshotListener { value, error ->
                scheduleList.clear()
                error?.printStackTrace()
                value?.documents?.map { documents ->
                    if (documents.exists()) {
                        val schedule = documents.toObject(Schedule::class.java)
                        if (schedule != null) {
                            scheduleList.add(schedule)
                        }
                    }
                }
                scheduleAdapter = ScheduleAdapter(binding.root.context,scheduleList)
                binding.recyclerviewSchedule.adapter = scheduleAdapter
            }

    }

    override fun onStart() {
        super.onStart()
        getCollectorInfo(FirebaseAuth.getInstance().currentUser!!.uid)
    }

}