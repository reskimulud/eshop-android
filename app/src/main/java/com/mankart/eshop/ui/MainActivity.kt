package com.mankart.eshop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mankart.eshop.R
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
                com.mankart.eshop.product.R.id.detailProductFragment -> hideBottomNavigation(true)
                // TODO : Customize the progress fragment
                // R.id.favorite_nav_graph ->
                com.mankart.eshop.cart.R.id.cartFragment -> hideBottomNavigation(true)
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