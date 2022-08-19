package com.mankart.eshop.core.domain.repository

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    fun getProfile(): Flow<Resource<User>>
    fun postReview(productId: String, rate: Int, review: String): Flow<Resource<String>>
    suspend fun logout()
    suspend fun getName(): Flow<String>
    suspend fun getEmail(): Flow<String>
    suspend fun saveName(name: String)
    suspend fun saveEmail(email: String)
}