package com.mankart.eshop.auth.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mankart.eshop.auth.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_host)
    }
}