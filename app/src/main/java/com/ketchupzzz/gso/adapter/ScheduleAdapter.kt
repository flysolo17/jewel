package com.ketchupzzz.gso.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.model.Collector
import com.ketchupzzz.gso.model.Schedule

class ScheduleAdapter(val context: Context,val scheduleList : List<Schedule>,val onScheduleClicks: OnScheduleClicks) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    interface OnScheduleClicks {
        fun onScheduleClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.row_scheds,parent,false)

        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val scheds  = scheduleList[position]
        val minute = String.format("%02d", scheds.startTime!!.minute)
        holder.textTime.text = "${scheds.startTime.hour} : $minute ${scheds.startTime.meridiem}"
        scheds.days?.map { days ->
            addDays(days,holder.layoutDays)
        }
        holder.getCollectorInfo(scheds.collectorID!!)
        holder.itemView.setOnClickListener{
            onScheduleClicks.onScheduleClick(position)
        }
        holder.initRoutes(scheds.routes)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textCollector : TextView = itemView.findViewById(R.id.textCollectorName)
        val textTime : TextView = itemView.findViewById(R.id.textTime)
        val textPlateNumber : TextView = itemView.findViewById(R.id.textCollectorPlateNumber)
        val layoutDays : LinearLayout = itemView.findViewById(R.id.layoutDays)
        val layoutRoutes : RecyclerView = itemView.findViewById(R.id.layoutRoutes)

        private lateinit var routeAdapter: RouteAdapter
        fun getCollectorInfo(collectorID : String) {
            FirebaseFirestore.getInstance().collection(Collector.TABLE_NAME)
                .document(collectorID)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val collector = document.toObject(Collector::class.java)
                        if (collector != null) {
                            textCollector.text = "${collector.firstName} ${collector.lastName}"
                            textPlateNumber.text = "${collector.plateNumber}"
                        }
                    }
                }
        }
        fun initRoutes(listRoutes : List<String>) {
            routeAdapter = RouteAdapter(itemView.context,listRoutes)
            layoutRoutes.apply {
                layoutManager = StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL)
                adapter = routeAdapter
            }



        }
    }
    private fun addDays(days: String ,layoutDays : LinearLayout) {
        val view = LayoutInflater.from(context).inflate(R.layout.row_days,null,false)
        view.setPadding(10)
        val textName : TextView= view.findViewById(R.id.texDayName)
        val buttonDays: MaterialCardView = view.findViewById(R.id.buttonDays)
        textName.text = days
        buttonDays.setCardBackgroundColor(Color.parseColor("#FAFAFF"))
        layoutDays.addView(view)
    }
}