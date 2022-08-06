package com.mankart.eshop.core.domain.usecase.authentication

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationUseCase {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun register(email: String, password: String): Flow<Resource<String>>
}