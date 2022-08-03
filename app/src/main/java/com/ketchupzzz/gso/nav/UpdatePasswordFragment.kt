package com.ketchupzzz.gso.nav

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.FragmentUpdatePasswordBinding
import com.ketchupzzz.gso.login.LoginActivity
import com.ketchupzzz.gso.model.Gso
import com.ketchupzzz.gso.viewmodels.GsoViewModel


class UpdatePasswordFragment : Fragment() {

    private lateinit var binding : FragmentUpdatePasswordBinding
    private lateinit var gsoViewModel: GsoViewModel
    private var gso : Gso? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdatePasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gsoViewModel = ViewModelProvider(requireActivity()).get(GsoViewModel::class.java)
        gsoViewModel.getGso().observe(viewLifecycleOwner) { gso ->
            this.gso = gso
        }
        binding.buttonChangePassword.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val current = binding.inputCurrent.text.toString()
            if (current.isEmpty()) {
                binding.inputCurrent.error = "Enter Current Password"
            }
            authenticateOldPassword(user,current)
        }
    }

    private fun authenticateOldPassword(user: FirebaseUser?, oldPassword: String) {
        if (user != null && user.email != null) {
            val authCredential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            user.reauthenticate(authCredential).addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    val newPassword: String = binding.inputNew.text.toString()
                    val confirmPassword: String = binding.inputConfirm.text.toString()
                    if (newPassword.length < 7) {
                        binding.inputNew.error = "Password too weak!"
                    } else if (newPassword != confirmPassword) {
                        binding.inputConfirm.error = "Password didn't match!"
                    } else {
                        updatePassword(user, newPassword)
                    }
                } else {
                    binding.inputCurrent.error = "Invalid password"
                }
            }
        }
    }

    private fun updatePassword(user: FirebaseUser, newPassword: String) {
        user.updatePassword(newPassword).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                Toast.makeText(
                    binding.root.context,
                    "Password updated Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                showSuccessDialog()
            } else {
                Toast.makeText(binding.root.context, "Failed!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e: Exception ->
            Toast.makeText(
                binding.root.context, "Failed: " + e.message, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSuccessDialog() {
        val alertDialog = AlertDialog.Builder(binding.root.context)
        alertDialog
            .setTitle("Stay Logged in?")
            .setMessage("password updated successfully, do you want to continue logged in ?")
            .setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                startActivity(Intent(requireActivity(),LoginActivity::class.java))
            }.setNegativeButton("Logout") { _: DialogInterface?, _: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }.show()
    }

}