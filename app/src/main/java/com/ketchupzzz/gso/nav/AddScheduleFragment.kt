package com.ketchupzzz.gso.nav

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.FragmentAddScheduleBinding
import com.ketchupzzz.gso.model.Days
import com.ketchupzzz.gso.model.Meridiem
import org.w3c.dom.Text


class AddScheduleFragment : Fragment() {

    private var binding : FragmentAddScheduleBinding? = null
    private val meridiem = Meridiem()
    private val days = Days()

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
        binding!!.buttonCancel.setOnClickListener {  Navigation.findNavController(view).popBackStack()}
        binding!!.buttonCreateSched.setOnClickListener {
            days.getDays().map { days ->
                if (days.isClick == true) {
                    Toast.makeText(view.context,"${days.day}",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun init() {
        meridiem.initMeridiem()
        days.initDays()
        //hour picker
        binding!!.hourPicker.minValue = 1
        binding!!.hourPicker.maxValue = 12
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