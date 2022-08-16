package com.mankart.eshop.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        val delayMillis = 2000L

        Handler(Looper.getMainLooper()).postDelayed({
            handlerIntent()
        }, delayMillis)
    }

    private fun handlerIntent() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.getUserToken().collect {
                Log.e("SplashScreenActivity", "User Token: $it")
                if (it.isNotEmpty() && it != "not_set_yet") {
                    profileSetup()
                    startActivity(
                        Intent(
                            this@SplashScreenActivity,
                            MainActivity::class.java,
                        )
                    )
                } else {
                    startActivity(
                        Intent(
                            this@SplashScreenActivity,
                            Class.forName("com.mankart.eshop.auth.AuthenticationHostActivity"),
                        )
                    )
                }
                finish()
            }
        }
    }

    private fun profileSetup() {
        lifecycleScope.launch {
            mainViewModel.getProfileData().collect {
                if (it is Resource.Success) {
                    mainViewModel.apply {
                        it.data?.name?.let { name -> saveUserName(name) }
                        it.data?.email?.let { email -> saveEmail(email) }
                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}