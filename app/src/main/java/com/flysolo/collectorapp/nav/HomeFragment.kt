package com.flysolo.collectorapp.nav

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flysolo.collectorapp.R
import com.flysolo.collectorapp.adapters.DestinationAdapter
import com.flysolo.collectorapp.databinding.FragmentHomeBinding
import com.flysolo.collectorapp.models.Destination
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class HomeFragment : Fragment(),DestinationAdapter.DestinationClicks {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentHomeBinding
    private lateinit var destinationAdapter: DestinationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        destinationAdapter = DestinationAdapter(view.context, getAllDestination(),this)
        binding.recyclerviewDestinations.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerviewDestinations.adapter = destinationAdapter
    }
    private fun getAllDestination(): FirestoreRecyclerOptions<Destination?> {
        val query: Query = firestore
            .collection("Destinations")
            .orderBy("timestamp",Query.Direction.ASCENDING)
            .limit(1)
        return FirestoreRecyclerOptions.Builder<Destination>()
            .setQuery(query, Destination::class.java)
            .build()
    }

    override fun onStart() {
        super.onStart()
        destinationAdapter.startListening()
    }

    override fun onNextButtonClick(position: Int) {
        if (destinationAdapter.getItem(position).listAddresses!!.size - 1 > destinationAdapter.getItem(position).isNowCollecting!!) {
            updateDestination(
                destinationAdapter.getItem(position).destinationID!!,
                destinationAdapter.getItem(position).isNowCollecting!!
            )
        } else {
            Toast.makeText(binding.root.context,"You've Reach your last destination",Toast.LENGTH_SHORT).show()
        }
    }



    override fun onDoneButtonClick(position: Int) {
        doneCollecting(destinationAdapter.getItem(position).destinationID!!)
    }
    private fun doneCollecting(id : String) {
        firestore.collection("Destinations")
            .document(id)
            .delete().addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(binding.root.context, "Done!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(binding.root.context, "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun updateDestination(id : String,nowCollecting : Int) {
        FirebaseFirestore.getInstance().collection("Destinations")
            .document(id)
            .update("nowCollecting",nowCollecting + 1).addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Toast.makeText(binding.root.context, "Destination updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(binding.root.context, "Failed to update address", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


