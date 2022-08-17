package com.mankart.eshop.auth.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.authentication.AuthenticationUseCase
import com.mankart.eshop.core.domain.usecase.profile.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase,
    private val profileUseCase: ProfileUseCase, // profile use case di provide oleh profile module
): ViewModel() {
    fun login(email: String, password: String) = authUseCase.login(email, password)
    fun register(name: String, email: String, password: String) = authUseCase.register(name, email, password)

    fun getProfileData() = profileUseCase.getUserProfile()
    suspend fun saveUserName(name: String) = profileUseCase.saveName(name)
    suspend fun saveEmail(email: String) = profileUseCase.saveEmail(email)
}