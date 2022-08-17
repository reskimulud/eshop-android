package com.mankart.eshop.setting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mankart.eshop.core.domain.usecase.profile.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase // profile useCase di provide oleh module profile
): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            profileUseCase.logout()
        }
    }
}