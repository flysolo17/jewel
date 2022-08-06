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
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.ketchupzzz.gso.MainActivity
import com.ketchupzzz.gso.ProgressDialog
import com.ketchupzzz.gso.R
import com.ketchupzzz.gso.databinding.ActivityLoginBinding
import com.ketchupzzz.gso.dialog.ForgotPasswordFragment
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var progressDialog : ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUp(binding.textCreateAccount)

        progressDialog = ProgressDialog(this)
        binding.buttonForgotPassword.setOnClickListener {
            val forgotPassword = ForgotPasswordFragment()
            if (!forgotPassword.isAdded) {
                forgotPassword.show(supportFragmentManager,"Forgot Password")
            }
        }
        binding.buttonLoginAccount.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.inputEmail.error = "enter email"
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.inputEmail.error = "invalid email"
                }
                password.isEmpty() -> {
                    binding.inputPassword.error = "enter password"
                }
                else -> {
                    loginUser(email,password)
                }
            }
        }
        binding.textCreateAccount.setOnClickListener { startActivity(Intent(this,CreateAccountActivity::class.java)) }
    }
    private fun signUp( textview : TextView) {
        val ss = SpannableString(getString(R.string.not_a_member_sign_up))
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(Intent(this@LoginActivity,CreateAccountActivity::class.java))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#29609B")
                ds.isUnderlineText = true
            }
        }
        ss.setSpan(clickableSpan1, 15, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textview.text = ss
        textview.movementMethod = LinkMovementMethod.getInstance()
    }
    //TODO: login script
    private fun loginUser(email: String, password: String) {
        progressDialog.isLoading()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                progressDialog.stopLoading()
                Toast.makeText(applicationContext, "login Success", Toast.LENGTH_SHORT).show()
                val currentUser = task.result?.user
                updateUI(currentUser)
            }
            if (!task.isSuccessful) {
                try {
                    throw Objects.requireNonNull(task.exception!!)
                } // if user enters wrong email.
                catch (invalidEmail: FirebaseAuthInvalidUserException) {
                    Log.d(
                        TAG,
                        "onComplete: invalid_email"
                    )
                    Toast.makeText(applicationContext, "Invalid Email", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.stopLoading()
                    // TODO: take your actions!
                } // if user enters wrong password.
                catch (wrongPassword: FirebaseAuthInvalidCredentialsException) {
                    Log.d(
                        TAG,
                        "onComplete: wrong_password"
                    )
                    Toast.makeText(applicationContext, "Wrong Password", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.stopLoading()
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "onComplete: " + e.message
                    )
                    progressDialog.stopLoading()
                }
            }
        }
    }


    private fun updateUI(currentUser : FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
    }
    companion object {
        const val TAG = "LoginActivity"
    }
}