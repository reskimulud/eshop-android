package com.mankart.eshop.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.authentication.AuthenticationUseCase
import com.mankart.eshop.core.domain.usecase.profile.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    fun getUserToken(): Flow<String> = authenticationUseCase.getUserToken()
    fun getProfileData() = profileUseCase.getUserProfile()
    suspend fun saveUserName(name: String) = profileUseCase.saveName(name)
    suspend fun saveEmail(email: String) = profileUseCase.saveEmail(email)
}