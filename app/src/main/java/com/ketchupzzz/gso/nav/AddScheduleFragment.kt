package com.ketchupzzz.gso.nav

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.FragmentAddScheduleBinding
import com.ketchupzzz.gso.model.*
import java.util.*


class AddScheduleFragment : Fragment() {
    private var selectedPosition : Int? = null
    private var binding : FragmentAddScheduleBinding? = null
    private val meridiem = Meridiem()
    private val days = Days()
    private var collectorList : MutableList<Collector> ?= null
    private var collectorNames : MutableList<String> ?= null
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var checkedRoutes: MutableList<String>

    private var routes: Array<String>?  = null
    private var checkedItems :BooleanArray? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddScheduleBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        routes = resources.getStringArray(R.array.routes)
        checkedItems = BooleanArray(routes!!.size)
        checkedRoutes = mutableListOf()
        binding!!.buttonCancel.setOnClickListener {  Navigation.findNavController(view).popBackStack()}

        binding!!.buttonCreateSched.setOnClickListener {

           if(getSelectedDays(days).isEmpty()) {
                Snackbar.make(view,"Pick days",Snackbar.LENGTH_SHORT).show()
            } else if (selectedPosition == null) {
                Snackbar.make(view,"No Collector",Snackbar.LENGTH_SHORT).show()
            } else if (checkedRoutes.size == 0) {
               Snackbar.make(view,"No Routes",Snackbar.LENGTH_SHORT).show()
            } else{
                val schedule = Schedule(
                    id = firestore.collection(Schedule.TABLE_NAME).document().id,
                    gsoID = FirebaseAuth.getInstance().currentUser!!.uid,
                    collectorID = collectorList!![selectedPosition!!].id,
                    days = getSelectedDays(days),
                    startTime = Time(binding!!.hourPicker.value,binding!!.minutePicker.value,meridiem.getMeridiemName()[binding!!.meridiemPicker.value]),
                    routes = checkedRoutes
                )
                addSched(schedule)
            }
        }
        binding!!.buttonPickCollector.setOnClickListener {
            showCollectorDialog()
        }
        binding!!.buttonAddCollectorRoutes.setOnClickListener {
            showRoutesDialog()
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
    private fun showCollectorDialog() {
        MaterialAlertDialogBuilder(binding!!.root.context)
            .setTitle("Choose a Collector")
            .setItems(collectorNames!!.toTypedArray()) { dialog , which ->
                selectedPosition = which
                binding!!.textCollectorName.text = collectorNames!![selectedPosition!!]
                binding!!.textCollectorPlateNumber.text = collectorList!![selectedPosition!!].plateNumber
            }
            .show()
    }


    private fun showRoutesDialog() {
        MaterialAlertDialogBuilder(binding!!.root.context)
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
                binding!!.layoutRoutes.removeAllViews()
                checkedRoutes.map { routes ->
                    display(routes)
                }
            }
            .show()
    }
    private fun display(route : String) {
        val view = layoutInflater.inflate(R.layout.row_routes,binding!!.root,false)
        val textName : TextView= view.findViewById(R.id.textRoute)
        textName.text = route
        binding!!.layoutRoutes.addView(view)
    }
    private fun getAllCollector(){
        collectorList = mutableListOf()
        collectorNames = mutableListOf()
        firestore.collection(Collector.TABLE_NAME)
            .addSnapshotListener { value, error ->
                collectorList!!.clear()
                collectorNames!!.clear()
                if (error != null) {
                    error.printStackTrace()
                } else {
                    value?.map { document ->
                        if (document != null) {
                            val collector = document.toObject(Collector::class.java)
                            collectorList?.add(collector)
                            collectorNames?.add("${collector.firstName} ${collector.lastName}")
                        }
                    }
                }
            }
    }

    private fun init() {
        getAllCollector()
        meridiem.initMeridiem()
        days.initDays()

        //hour picker
        binding!!.hourPicker.minValue = 1
        binding!!.hourPicker.maxValue = 12
        binding!!.hourPicker.showDividers = 0
        binding!!.minutePicker.setFormatter { i -> String.format("%02d", i) } //format the number

        //minute picker
        binding!!.minutePicker.minValue = 0
        binding!!.minutePicker.maxValue = 60

        //am/pm picker
        binding!!.meridiemPicker.minValue = 0
        binding!!.meridiemPicker.maxValue = meridiem.getMeridiemList().size -1
        binding!!.meridiemPicker.displayedValues = meridiem.getMeridiemName().toTypedArray()

        //display days
        days.getDays().map { days ->
            addDay(days)
        }
    }
    private fun addDay(days: Days) {
        val view = layoutInflater.inflate(R.layout.row_days,binding!!.root,false)
        val textName : TextView= view.findViewById(R.id.texDayName)
        val buttonDays: MaterialCardView = view.findViewById(R.id.buttonDays)
        textName.text = days.day
        if (days.isClick == true) {
            buttonDays.setCardBackgroundColor(Color.parseColor("#FAFAFF"))
        } else {
            buttonDays.setCardBackgroundColor(Color.WHITE)
        }

        buttonDays.setOnClickListener {
            days.isClick = days.isClick != true
            updateButtonColor(textName,buttonDays, days.isClick!!)
        }
        binding!!.layoutDays.addView(view)
    }

    private fun addSched(schedule: Schedule) {
        firestore.collection(Schedule.TABLE_NAME)
            .document(schedule.id!!)
            .set(schedule)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(view?.context,"Success",Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding!!.root).popBackStack()
                } else {
                    Toast.makeText(view?.context,"Failed",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateButtonColor(text:TextView, button : MaterialCardView,isClick : Boolean) {
        if (isClick) {
            text.setTextColor(Color.WHITE)
            button.setCardBackgroundColor(resources.getColor(R.color.purple_500))
        } else {
            text.setTextColor(Color.BLACK)
            button.setCardBackgroundColor(Color.WHITE)
        }
    }

}