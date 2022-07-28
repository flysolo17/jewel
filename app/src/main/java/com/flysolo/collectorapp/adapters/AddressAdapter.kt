package com.flysolo.collectorapp.adapters

import android.content.Context
import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.RecyclerView
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.viewmodel.AdressViewModel

class AddressAdapter(private val context: Context, var listAddress : List<String>) :
    RecyclerView.Adapter<AddressAdapter.AddressAdapterViewHolder>() {
    inner class AddressAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAddress: TextView
        init {
            textAddress  = itemView.findViewById(R.id.textAddress)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_addresses, parent, false)
        return AddressAdapterViewHolder(view)
    }
    private lateinit var addressViewModel : AdressViewModel
    override fun onBindViewHolder(holder: AddressAdapterViewHolder, position: Int) {
        val activity = context as FragmentActivity
        addressViewModel = ViewModelProvider(activity).get(AdressViewModel::class.java)
        val lifecycle = activity as LifecycleOwner
        addressViewModel.getSelected().observe(lifecycle) { selected ->
            if (selected == position) {
                holder.textAddress.setTextColor(Color.BLUE)
            } else {
                holder.textAddress.setTextColor(Color.BLACK)
            }
        }
        holder.textAddress.text = listAddress[position]

    }

    override fun getItemCount(): Int {
        return listAddress.size
    }


}