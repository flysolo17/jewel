package com.flysolo.collectorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flysolo.collectorapp.R

class RouteAdapter(val context: Context, val routeList : List<String>) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_routes,parent,false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.textRoute.text = routeList[position]
    }

    override fun getItemCount(): Int {
        return routeList.size
    }
    class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textRoute : TextView = itemView.findViewById(R.id.textRoute)

    }

}