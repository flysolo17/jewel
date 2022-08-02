package com.ketchupzzz.gso.nav

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.FragmentAddScheduleBinding
import com.ketchupzzz.gso.databinding.FragmentUpdateScheduleBinding
import com.ketchupzzz.gso.model.*
import com.ketchupzzz.gso.viewmodels.ScheduleViewModel

class UpdateScheduleFragment : Fragment() {
    private lateinit var binding : FragmentUpdateScheduleBinding
    private val meridiem = Meridiem()
    private val days = Days()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var checkedRoutes: MutableList<String>
    private var routes: Array<String>?  = null
    private var checkedItems :BooleanArray? = null
    private var scheduleViewModel: ScheduleViewModel ? = null
    private var schedule : Schedule? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateScheduleBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        routes = resources.getStringArray(R.array.routes)
        checkedItems = BooleanArray(routes!!.size)
        checkedRoutes = mutableListOf()

        scheduleViewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        scheduleViewModel!!.getSchedule().observe(viewLifecycleOwner) { schedule ->

            this.schedule = schedule
            displayDefault(schedule)
            displayCollectorInfo(schedule.collectorID!!)
        }

        binding.buttonCancel.setOnClickListener {  Navigation.findNavController(view).popBackStack()}

        binding.buttonCreateSched.setOnClickListener {
            if(getSelectedDays(days).isEmpty()) {
                Snackbar.make(view,"Pick days", Snackbar.LENGTH_SHORT).show()
            } else if (checkedRoutes.size == 0) {
                Snackbar.make(view,"No Routes", Snackbar.LENGTH_SHORT).show()
            } else{
                val schedule = Schedule(
                    id = this.schedule?.id,
                    gsoID = FirebaseAuth.getInstance().currentUser!!.uid,
                    collectorID = this.schedule?.collectorID,
                    days = getSelectedDays(days),
                    startTime = Time(binding.hourPicker.value, binding.minutePicker.value,meridiem.getMeridiemName()[binding.meridiemPicker.value]),
                    routes = checkedRoutes
                )
                addSched(schedule)
            }
        }

        binding.buttonAddCollectorRoutes.setOnClickListener {
            showRoutesDialog()
        }
    }
    private fun displayDefault(schedule: Schedule) {

        checkedRoutes.clear()
        binding.hourPicker.value = schedule.startTime?.hour!!
        binding.minutePicker.value = schedule.startTime.minute!!

        //display days
        days.getDays().map { day ->
            if (schedule.days!!.contains(day.day)) {
                day.isClick = true
            }
            addDay(day)
        }
        routes?.mapIndexed { index, s ->
            if (schedule.routes.contains(s)) {
                checkedItems!![index] = true
                checkedRoutes.add(s)
                displayRoute(route = s)
            }
        }

        meridiem.getMeridiemList().toTypedArray().mapIndexed { index, meridiem ->
            if (meridiem.name.equals(schedule.startTime.meridiem)) {
                binding.meridiemPicker.value = index
            }
        }

    }
    private fun getSelectedDays(days : Days)  : List<String>{
        val list : MutableList<String> = mutableListOf()
        days.getDays().map { days ->
            if (days.isClick == true) {
                list.add(days.day!!)
            }

        }
        return list
    }
    private fun showRoutesDialog() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Choose a Collector")
            .setMultiChoiceItems(routes,checkedItems) { dialog , which ,checked->3
                checkedItems!![which] = checked
                if (checkedItems!![which] && !checkedRoutes.contains(routes!![which])) {
                    checkedRoutes.add(routes!![which])
                } else {
                    checkedRoutes.remove(routes!![which])

                }
            }

            .setPositiveButton("Ok") { _, _ ->
                binding.layoutRoutes.removeAllViews()
                checkedRoutes.map { routes ->
                    displayRoute(routes)
                }
            }
            .show()
    }
    private fun displayRoute(route : String) {
        val view = layoutInflater.inflate(R.layout.row_routes, binding.root,false)
        val textName : TextView= view.findViewById(R.id.textRoute)
        textName.text = route
        binding.layoutRoutes.addView(view)
    }

    private fun displayCollectorInfo(collectorID : String){
        firestore.collection(Collector.TABLE_NAME)
            .document(collectorID)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    error.printStackTrace()
                } else {
                    if (value != null) {
                        if (value.exists()) {
                            val collector = value.toObject(Collector::class.java)
                            if (collector != null) {
                                binding.textCollectorPlateNumber.text = collector.plateNumber
                                binding.textCollectorName.text = "${collector.firstName} ${collector.lastName}"
                            }
                        }
                    }
                }
            }
    }


    private fun init() {
        meridiem.initMeridiem()
        days.initDays()

        //hour picker
        binding.hourPicker.minValue = 1
        binding.hourPicker.maxValue = 12
        binding.hourPicker.showDividers = 0
        binding.minutePicker.setFormatter { i -> String.format("%02d", i) } //format the number

        //minute picker
        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 60

        //am/pm picker
        binding.meridiemPicker.minValue = 0
        binding.meridiemPicker.maxValue = meridiem.getMeridiemList().size -1
        binding.meridiemPicker.displayedValues = meridiem.getMeridiemName().toTypedArray()



    }
    private fun addDay(days: Days) {
        val view = layoutInflater.inflate(R.layout.row_days, binding.root,false)
        val textName : TextView = view.findViewById(R.id.texDayName)
        val buttonDays: MaterialCardView = view.findViewById(R.id.buttonDays)
        textName.text = days.day
        updateButtonColor(textName,buttonDays, isClick = days.isClick!!)
        buttonDays.setOnClickListener {
            days.isClick = days.isClick != true
            updateButtonColor(textName,buttonDays, days.isClick!!)
        }
        binding.layoutDays.addView(view)
    }

    private fun addSched(schedule: Schedule) {
        firestore.collection(Schedule.TABLE_NAME)
            .document(schedule.id!!)
            .set(schedule)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(binding.root.context,"Success", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).popBackStack()
                } else {
                    Toast.makeText(binding.root.context,"Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateButtonColor(text: TextView, button : MaterialCardView, isClick : Boolean) {
        if (isClick) {
            text.setTextColor(Color.WHITE)
            button.setCardBackgroundColor(resources.getColor(R.color.purple_500))
        } else {
            text.setTextColor(Color.BLACK)
            button.setCardBackgroundColor(Color.WHITE)
        }
    }



}