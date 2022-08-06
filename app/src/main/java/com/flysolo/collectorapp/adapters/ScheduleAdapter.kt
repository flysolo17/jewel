package com.flysolo.collectorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.models.Schedule
import com.google.android.material.card.MaterialCardView

class ScheduleAdapter(val context: Context, private val scheduleList : List<Schedule>) :
        RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_scheds,parent,false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.initRoutes(schedule.routes)
        schedule.days?.map { days ->
            addDays(days,holder.layoutDays)
        }
        holder.textTime.text = "${schedule.startTime?.hour} : ${schedule.startTime?.minute} ${schedule.startTime?.meridiem}"
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutDays : LinearLayout  = itemView.findViewById(R.id.layoutDays)
        private val layoutRoutes : RecyclerView = itemView.findViewById(R.id.layoutRoutes)
        private lateinit var routeAdapter: RouteAdapter
        val textTime: TextView = itemView.findViewById(R.id.textTime)
        fun initRoutes(listRoutes : List<String>) {
            routeAdapter = RouteAdapter(itemView.context,listRoutes)
            layoutRoutes.apply {
                layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                adapter = routeAdapter
            }

        }
    }
    private fun addDays(days: String ,layoutDays : LinearLayout) {
        val view = LayoutInflater.from(context).inflate(R.layout.row_days,layoutDays,false)
        val textName : TextView = view.findViewById(R.id.texDayName)

        textName.text = days

        layoutDays.addView(view)
    }
}