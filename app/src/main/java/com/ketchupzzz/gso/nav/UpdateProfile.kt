package com.ketchupzzz.gso.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.FragmentUpdateProfileBinding
import com.ketchupzzz.gso.model.Gso
import com.ketchupzzz.gso.viewmodels.GsoViewModel


class UpdateProfile : Fragment() {
    private lateinit var binding : FragmentUpdateProfileBinding
    private lateinit var gsoViewModel: GsoViewModel
    private var gso : Gso? = null
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gsoViewModel = ViewModelProvider(requireActivity())[GsoViewModel::class.java]
        gsoViewModel.getGso().observe(viewLifecycleOwner) { gso ->
            this.gso = gso
            displayGsoInfo(gso)
        }
        binding.buttonUpdateAccount.setOnClickListener {
            val firstname = binding.inputFirstname.text.toString()
            val lastname = binding.inputLastname.text.toString()
            val phone = binding.inputPhoneNumber.text.toString()
            if (firstname.isEmpty()) {
                binding.inputFirstname.error = "No Firstname"
            }else if (lastname.isEmpty()) {
                binding.inputLastname.error = "No lastname"
            } else  if (phone.isEmpty()) {
                binding.inputPhoneNumber.error = "No phone number"
            } else {
                val newGSo = Gso(gso?.id,firstname,lastname,phone, gso?.email)
                updateProfile(newGSo)
            }
        }
    }

    private fun updateProfile(newGSo: Gso) {
        firestore.collection(Gso.TABLE_NAME)
            .document(newGSo.id!!)
            .set(newGSo)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(binding.root.context,"Profile updated!",Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).popBackStack()
                } else {
                    Toast.makeText(binding.root.context,"failed to update profile",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun displayGsoInfo(gso: Gso?) {
        if (gso!= null) {
            binding.inputFirstname.setText(gso.firstName)
            binding.inputLastname.setText(gso.lastName)
            binding.inputPhoneNumber.setText(gso.phone)
        }
    }

}