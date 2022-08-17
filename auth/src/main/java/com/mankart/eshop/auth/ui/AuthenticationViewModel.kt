package com.mankart.eshop.auth.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.authentication.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val authUseCase: AuthenticationUseCase): ViewModel() {
    fun login(email: String, password: String) = authUseCase.login(email, password)
    fun register(name: String, email: String, password: String) = authUseCase.register(name, email, password)
}