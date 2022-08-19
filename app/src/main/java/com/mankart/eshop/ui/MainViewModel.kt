package com.mankart.eshop.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.authentication.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
) : ViewModel() {
    fun getUserToken(): Flow<String> = authenticationUseCase.getUserToken()
}