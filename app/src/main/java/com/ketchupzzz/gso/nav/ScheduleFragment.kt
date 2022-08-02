package com.ketchupzzz.gso.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.adapter.ScheduleAdapter
import com.ketchupzzz.gso.databinding.FragmentScheduleBinding
import com.ketchupzzz.gso.model.Schedule
import com.ketchupzzz.gso.viewmodels.ScheduleViewModel


class ScheduleFragment : Fragment() , ScheduleAdapter.OnScheduleClicks{

    private lateinit var binding: FragmentScheduleBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var scheduleAdapter : ScheduleAdapter
    private lateinit var scheduleList : MutableList<Schedule>
    private lateinit var scheduleViewModel : ScheduleViewModel
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
        scheduleViewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        init()
        getAllSchedules(FirebaseAuth.getInstance().currentUser!!.uid)
        swipeToDelete(binding.recyclerviewScheds)
    }

    private fun getAllSchedules(myID : String) {
        scheduleList = mutableListOf()
        firestore.collection(Schedule.TABLE_NAME)
            .addSnapshotListener { value, error ->
                scheduleList.clear()
                if (error != null) {
                    error.printStackTrace()
                } else {
                    value?.map { documents ->
                        val schedule = documents.toObject(Schedule::class.java)
                        if (schedule.gsoID.equals(myID)) {
                            scheduleList.add(schedule)
                        }
                    }
                    scheduleAdapter = ScheduleAdapter(binding.root.context,scheduleList,this)
                    binding.recyclerviewScheds.adapter = scheduleAdapter
                }
            }
    }

    private fun deleteSchedule(scheduleID : String) {
        firestore.collection(Schedule.TABLE_NAME)
            .document(scheduleID)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(binding.root.context,"Schedule Deleted!",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(binding.root.context,"Unsuccessful:  Deletion!",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun swipeToDelete(recyclerView: RecyclerView?) {
        val callback = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle("Delete Schedule")
                    .setMessage("Are you sure you want to delete this schedule?")
                    .setPositiveButton("Yes") { _,_ ->
                        deleteSchedule(scheduleList[position].id!!)
                        scheduleAdapter.notifyItemRemoved(position)
                    }
                    .setNegativeButton("No") { dialog,_->
                        dialog.dismiss()
                        Toast.makeText(binding.root.context,"Cancelled",Toast.LENGTH_SHORT).show()
                        scheduleAdapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        scheduleAdapter.notifyItemChanged(position)
                    }.show()
            }
        })
        callback.attachToRecyclerView(recyclerView)
    }

    override fun onScheduleClick(position: Int) {
        scheduleViewModel.setSchedule(scheduleList[position])
        Navigation.findNavController(binding.root).navigate(R.id.action_menu_schedule_to_updateScheduleFragment)
    }
}