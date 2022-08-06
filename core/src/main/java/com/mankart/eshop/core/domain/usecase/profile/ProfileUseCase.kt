package com.mankart.eshop.core.domain.usecase.profile

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileUseCase {
    fun getUserProfile(): Flow<Resource<User>>
    suspend fun logout()
}