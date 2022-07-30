package com.flysolo.collectorapp.nav

import android.R
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flysolo.collectorapp.databinding.FragmentAddDestinationBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import android.util.SparseBooleanArray
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.flysolo.collectorapp.MainActivity
import com.flysolo.collectorapp.models.Destination
import com.flysolo.collectorapp.models.User
import com.google.firebase.auth.FirebaseAuth


class AddDestination : Fragment() {
    private lateinit var binding: FragmentAddDestinationBinding
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var arrayAdapter: ArrayAdapter<*>
    val listAddresses = MainActivity.address!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddDestinationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arrayAdapter = ArrayAdapter(view.context,R.layout.simple_list_item_multiple_choice,
            listAddresses)
        binding.listHomeAddress.adapter = arrayAdapter

        binding.listHomeAddress.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        binding.listHomeAddress.setOnItemClickListener { parent, view, position, id ->
            binding.locations.text = listAddresses[position]
        }
        binding.buttonCancel.setOnClickListener{
              Navigation.findNavController(view).popBackStack()
        }
        binding.buttonAddDestination.setOnClickListener{
            when {
                getCheckedPositions().isEmpty() -> {
                    Toast.makeText(requireContext(),"Select destinations",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val destination = Destination(
                        destinationID = db.collection("Destinations").document().id,
                        collectorID = FirebaseAuth.getInstance().currentUser!!.uid,
                        listAddresses = getCheckedPositions(),
                        isNowCollecting = 0,
                        System.currentTimeMillis())
                    addDestination(destination)
                }
            }


        }
    }
    private fun getCheckedPositions(): MutableList<String> {
        val listAddress = mutableListOf<String>()
        val len: Int = binding.listHomeAddress.count
        val checked: SparseBooleanArray = binding.listHomeAddress.checkedItemPositions
        for (i in 0 until len) if (checked[i]) {
            listAddress.add(listAddresses[i])
        }
        return listAddress
    }
    private fun addDestination(destination: Destination){
        db.collection("Destinations")
            .document(destination.destinationID!!)
            .set(destination)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Destination Added",Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).popBackStack()
                } else{
                    Toast.makeText(requireContext(),"failed",Toast.LENGTH_SHORT).show()

                }
            }
    }
}