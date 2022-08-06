package com.flysolo.collectorapp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flysolo.collectorapp.MainActivity
import com.flysolo.collectorapp.ProgressDialog
import com.flysolo.collectorapp.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        binding.buttonLogin.setOnClickListener {
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
        binding.buttonCreateAccount.setOnClickListener { startActivity(Intent(this,CreateAccountActivity::class.java)) }
        binding.textForgotPassword.setOnClickListener {
            val forgotPasswordFragment = ForgotPasswordFragment();
            if (!forgotPasswordFragment.isAdded) {
                forgotPasswordFragment.show(supportFragmentManager,"Forgot Password")
            }
        }
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