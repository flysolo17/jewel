package com.flysolo.collectorapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.models.Collector
import com.flysolo.collectorapp.models.Destination
import com.flysolo.collectorapp.viewmodel.AdressViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class DestinationAdapter(private val context: Context, options: FirestoreRecyclerOptions<Destination?>,val destinationClicks: DestinationClicks) : FirestoreRecyclerAdapter<Destination?, DestinationAdapter.DestinationViewHolder?>(options){
    private lateinit var firestore: FirebaseFirestore
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressViewModel : AdressViewModel
    interface DestinationClicks {
        fun onNextButtonClick(position: Int)
        fun onDoneButtonClick(position: Int)
    }
    inner class DestinationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textCollectorName : TextView
        var textDate : TextView
        var recyclerviewAddresses: RecyclerView
        var buttonSave : Button
         var buttonDone  : Button
        var buttonNext : Button
        init {
            textCollectorName = itemView.findViewById(R.id.collectorsName)
            textDate = itemView.findViewById(R.id.textDate)
            recyclerviewAddresses = itemView.findViewById(R.id.recyclerviewAddresses)
            buttonSave = itemView.findViewById(R.id.buttonSave)
            buttonDone = itemView.findViewById(R.id.buttonDone)
            buttonNext = itemView.findViewById(R.id.buttonNext)
        }
        fun getCollectorInfo(id : String) {
            FirebaseFirestore.getInstance().collection(Collector.TABLE_NAME)
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val collector = document.toObject(Collector::class.java)
                        if (collector != null) {
                            textCollectorName.text = "${collector.firstName} ${collector.lastName}"
                        }
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_destinations, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int, model: Destination) {
        val activity = context as FragmentActivity
        addressViewModel = ViewModelProvider(activity).get(AdressViewModel::class.java)
        firestore = FirebaseFirestore.getInstance()
        addressAdapter = AddressAdapter(context,model.listAddresses!!)
        addressViewModel.select(model.isNowCollecting!!)
        holder.recyclerviewAddresses.layoutManager = LinearLayoutManager(context)
        holder.recyclerviewAddresses.adapter = addressAdapter
        holder.textDate.text = timestampToDate(model.timestamp!!)
        holder.getCollectorInfo(model.collectorID!!)
        dragAddress(model.listAddresses!!,holder.recyclerviewAddresses)
        holder.buttonSave.setOnClickListener{
            addressViewModel.select(model.isNowCollecting!!)
            updateAddresses(context, model.destinationID!!,model.listAddresses!!)
        }
        holder.buttonDone.setOnClickListener{
            destinationClicks.onDoneButtonClick(position)
        }
        holder.buttonNext.setOnClickListener{
            addressViewModel.select(model.isNowCollecting!!)
           destinationClicks.onNextButtonClick(position)
        }

    }

    private fun dragAddress(list: List<String>, recyclerView: RecyclerView?) {
        val callback = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP or ItemTouchHelper.DOWN,0) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    val fromPosition : Int = viewHolder.bindingAdapterPosition
                    val toPosition : Int = target.bindingAdapterPosition
                    Collections.swap(list,fromPosition,toPosition)
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }
            })
        callback.attachToRecyclerView(recyclerView)
    }
    private fun updateAddresses(context: Context?, id : String, list: List<String>) {
        firestore.collection("Destinations")
            .document(id)
            .update("listAddresses",list).addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Address updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update list", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun timestampToDate(timestamp: Long): String? {
        val date = Date(timestamp)
        val format: Format = SimpleDateFormat("MMMM dd, hh:mm aa")
        return format.format(date)
    }

}