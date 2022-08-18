package com.mankart.eshop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mankart.eshop.R
import com.mankart.eshop.product.R as productR
import com.mankart.eshop.cart.R as cartR
import com.mankart.eshop.profile.R as profileR
import com.mankart.eshop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main_activity) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                productR.id.detailProductFragment -> hideBottomNavigation(true)
                cartR.id.cartFragment -> hideBottomNavigation(true)
                profileR.id.detailTransactionFragment -> hideBottomNavigation(true)
                else -> hideBottomNavigation(false)
            }
        }
    }

    private fun hideBottomNavigation(isHide: Boolean) {
        if (isHide) {
            binding.bottomNavigationView.visibility = BottomNavigationView.GONE
        } else {
            binding.bottomNavigationView.visibility = BottomNavigationView.VISIBLE
        }
    }
}