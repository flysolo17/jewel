package com.ketchupzzz.gso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ketchupzzz.gso.databinding.ActivityMainBinding
import com.ketchupzzz.gso.model.Meridiem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNav()
        binding.fabCreateSchedule.setOnClickListener {
            navController?.navigate(R.id.addScheduleFragment)
        }
    }
    private fun setupNav() {
        navController = findNavController(R.id.fragmentContainer)
        binding.gsoNav.setupWithNavController(navController!!)
        binding.gsoNav.background =null
        binding.gsoNav.menu[1].isEnabled = false
        navController!!.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.menu_schedule -> {
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
        binding.fabCreateSchedule.show()
    }
    private fun hideBottomNav() {
        binding.bottomAppBar.performHide(true)
        binding.bottomAppBar.hideOnScroll = false
        binding.fabCreateSchedule.hide()
    }
}