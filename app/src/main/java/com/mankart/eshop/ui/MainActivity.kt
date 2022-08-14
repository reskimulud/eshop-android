package com.mankart.eshop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.mankart.eshop.R
import com.mankart.eshop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

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

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                com.mankart.eshop.product.R.id.detailProductFragment -> bottomNavigation.visibility = BottomNavigationView.GONE
                R.id.favorite_nav_graph -> {
                    try {
                        installFavoriteModule()
                    } catch (err: Exception) {
                        Toast.makeText(this, "Module not found", Toast.LENGTH_LONG).show()
                        controller.currentDestination?.id?.let { navController.navigate(it) }
                    }
                }
                else -> bottomNavigation.visibility = BottomNavigationView.VISIBLE
            }
        }
    }

    private fun installFavoriteModule(){
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleFavorite = "favorite"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            return
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleFavorite)
                .build()

            Toast.makeText(this@MainActivity, "Installing Favorite Module...", Toast.LENGTH_SHORT).show()
            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    Toast.makeText(this@MainActivity, "Favorite Module Installed", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, "Favorite Module Installation Failed", Toast.LENGTH_SHORT).show()
                    throw it
                }
        }
    }
}