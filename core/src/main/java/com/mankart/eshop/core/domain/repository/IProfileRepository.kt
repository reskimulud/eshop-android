package com.mankart.eshop.core.domain.repository

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    fun getProfile(): Flow<Resource<User>>
    suspend fun logout()
}