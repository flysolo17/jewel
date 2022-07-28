package com.flysolo.collectorapp.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flysolo.collectorapp.MainActivity
import com.flysolo.collectorapp.ProgressDialog
import com.flysolo.collectorapp.databinding.ActivityCreateAccountBinding
import com.flysolo.collectorapp.models.Collector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCreateAccountBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        binding.buttonCreateAccount.setOnClickListener {
            val firstname = binding.inputFirstname.text.toString()
            val lastName = binding.inputLastname.text.toString()
            val phone = binding.inputPhoneNumber.text.toString()
            val plateNumber = binding.inputPlateNumber.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if (firstname.isEmpty()) {
                binding.inputFirstname.error = "enter firstname"
            } else if (lastName.isEmpty()) {
                binding.inputLastname.error = "enter lastname"
            }  else if (phone.isEmpty()) {
                binding.inputPhoneNumber.error = "enter phone"
            } else if (phone.length != 11) {
                binding.inputPhoneNumber.error = "invalid phone number"
            } else if (plateNumber.isEmpty()) {
                binding.inputPlateNumber.error = "enter plate number"
            } else if (email.isEmpty()) {
                binding.inputEmail.error = "enter email"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = "invalid email"
            }else if (password.isEmpty()) {
                binding.inputPassword.error = "enter password"
            } else {
                val collector = Collector(
                    firstName = firstname,
                    lastName = lastName,
                    phone = phone,
                    plateNumber = plateNumber,
                    email = email)
                loginWithEmailAndPassword(collector,password)
            }
        }
    }
    private fun loginWithEmailAndPassword(collector: Collector,password : String) {
        progressDialog.isLoading()
        firebaseAuth.createUserWithEmailAndPassword(collector.email!!,password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = task.result.user
                collector.id = currentUser!!.uid
                progressDialog.stopLoading()
                addCollectorAccount(collector,currentUser)
            } else {
                progressDialog.stopLoading()
                Toast.makeText(this,"Account Creation Failed" ,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addCollectorAccount(collector: Collector,currentUser : FirebaseUser) {
        progressDialog.isLoading()
        firestore.collection(Collector.TABLE_NAME)
            .document(collector.id!!)
            .set(collector)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.stopLoading()
                    Toast.makeText(this,"Account Added Successfully",Toast.LENGTH_SHORT).show()
                    updateUI(currentUser)
                } else {
                    progressDialog.stopLoading()
                    Toast.makeText(this,"Failed adding account info",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateUI(currentUser : FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}