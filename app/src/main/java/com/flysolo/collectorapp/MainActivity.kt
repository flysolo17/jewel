package com.flysolo.collectorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import androidx.recyclerview.widget.LinearLayoutManager

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.flysolo.collectorapp.adapters.DestinationAdapter
import com.flysolo.collectorapp.databinding.ActivityMainBinding
import com.flysolo.collectorapp.models.Destination

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNav()
        binding.fabCreateDestination.setOnClickListener {
            navController?.navigate(R.id.addDestination)
        }

    }
    private fun setupNav() {
        navController = findNavController(R.id.fragmentContainer)
        binding.collectorNav.setupWithNavController(navController!!)
        binding.collectorNav.background = null
        binding.collectorNav.menu[1].isEnabled =false
        navController!!.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.nav_home -> {
                    showBottomNav()
                }
                else -> {
                    hideBottomNav()
                }
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomAppBar.performShow(true)
        binding.bottomAppBar.hideOnScroll = true
        binding.fabCreateDestination.show()
    }
    private fun hideBottomNav() {
        binding.bottomAppBar.performHide(true)
        binding.bottomAppBar.hideOnScroll = false
        binding.fabCreateDestination.hide()
    }

}