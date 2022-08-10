package com.mankart.eshop.core.domain.usecase.authentication

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationUseCase {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun register(name: String, email: String, password: String): Flow<Resource<String>>
    fun getUserToken(): Flow<String>
}