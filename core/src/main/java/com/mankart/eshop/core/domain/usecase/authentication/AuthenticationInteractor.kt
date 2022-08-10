package com.mankart.eshop.core.domain.usecase.authentication

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import com.mankart.eshop.core.domain.repository.IAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthenticationInteractor @Inject constructor(private val repository: IAuthenticationRepository): AuthenticationUseCase {
    override fun login(email: String, password: String): Flow<Resource<User>> =
        repository.postLogin(email, password)

    override fun register(name: String, email: String, password: String): Flow<Resource<String>> =
        repository.postRegister(name, email, password)

    override fun getUserToken(): Flow<String> =
        repository.getUserToken()
}