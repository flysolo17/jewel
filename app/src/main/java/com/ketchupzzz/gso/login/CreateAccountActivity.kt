package com.ketchupzzz.gso.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzzz.gso.MainActivity
import com.ketchupzzz.gso.ProgressDialog
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.ActivityCreateAccountBinding
import com.ketchupzzz.gso.model.Gso

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login(binding.textButtonSignIN) //Give Color to Login in textview
        progressDialog = ProgressDialog(this)

        binding.buttonCreateAccount.setOnClickListener {
            val firstname = binding.inputFirstname.text.toString()
            val lastName = binding.inputLastname.text.toString()
            val phone = binding.inputPhoneNumber.text.toString()
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
            } else if (email.isEmpty()) {
                binding.inputEmail.error = "enter email"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = "invalid email"
            }else if (password.isEmpty()) {
                binding.inputPassword.error = "enter password"
            } else {
                val collector = Gso(
                    firstName = firstname,
                    lastName = lastName,
                    phone = phone,
                    email = email)
                loginWithEmailAndPassword(collector,password)
            }
        }
    }
    private fun login(textView : TextView) {
        val ss = SpannableString(getString(R.string.already_have_an_account_sign_in))
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(Intent(this@CreateAccountActivity,LoginActivity::class.java))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#29609B")
                ds.isUnderlineText = true
            }
        }
        ss.setSpan(clickableSpan1, 25, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
    private fun loginWithEmailAndPassword(gso: Gso,password : String) {
        progressDialog.isLoading()
        firebaseAuth.createUserWithEmailAndPassword(gso.email!!,password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = task.result.user
                gso.id = currentUser!!.uid
                progressDialog.stopLoading()
                addCollectorAccount(gso,currentUser)
            } else {
                progressDialog.stopLoading()
                Toast.makeText(this,"Account Creation Failed" , Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addCollectorAccount(gso: Gso,currentUser : FirebaseUser) {
        progressDialog.isLoading()
        firestore.collection(Gso.TABLE_NAME)
            .document(gso.id!!)
            .set(gso)
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}