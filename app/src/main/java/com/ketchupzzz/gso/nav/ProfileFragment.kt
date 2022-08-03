package com.ketchupzzz.gso.nav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.FragmentProfileBinding
import com.ketchupzzz.gso.databinding.FragmentScheduleBinding
import com.ketchupzzz.gso.login.LoginActivity
import com.ketchupzzz.gso.model.Gso
import com.ketchupzzz.gso.viewmodels.GsoViewModel


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var gsoViewModel: GsoViewModel
    private var gso : Gso? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gsoViewModel = ViewModelProvider(requireActivity())[GsoViewModel::class.java]
        getUserInfo(FirebaseAuth.getInstance().currentUser!!.uid)
        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity,LoginActivity::class.java))
            activity?.finish()
        }
        binding.buttonEditProfile.setOnClickListener {
            if (gso != null) {
                gsoViewModel.setGso(gso!!)
                Navigation.findNavController(view).navigate(R.id.action_menu_profile_to_updateProfile)
            } else {
                Toast.makeText(requireContext(),"No Gso!",Toast.LENGTH_SHORT).show()
            }

        }
        binding.buttonChangePassword.setOnClickListener {
            if (gso != null) {
                gsoViewModel.setGso(gso!!)
                Navigation.findNavController(view).navigate(R.id.action_menu_profile_to_updatePasswordFragment)
            } else {
                Toast.makeText(requireContext(),"No Gso!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserInfo(gsoID: String) {
        firestore.collection(Gso.TABLE_NAME)
            .document(gsoID)
            .addSnapshotListener { value, error ->
                error?.printStackTrace()
                if (value != null) {
                    if (value.exists()) {
                        val gso = value.toObject(Gso::class.java)
                        this.gso = gso
                        diplayGsoInfo(gso)
                    }
                }
            }
    }

    private fun diplayGsoInfo(gso: Gso?) {
        if (gso != null) {
            binding.textFullname.text = "${gso.firstName} ${gso.lastName}"
            binding.textEmail.text = "${gso.email}"
            binding.textPhone.text = "${gso.phone}"
        }
    }


}