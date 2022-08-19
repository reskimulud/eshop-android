package com.mankart.eshop.core.domain.repository

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthenticationRepository {
    fun postLogin(email: String, password: String): Flow<Resource<User>>
    fun postRegister(name: String, email: String, password: String): Flow<Resource<String>>
    fun getUserToken(): Flow<String>
}