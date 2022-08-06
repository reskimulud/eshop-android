package com.mankart.eshop.core.domain.usecase.profile

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import com.mankart.eshop.core.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileInteractor @Inject constructor(private val repository: IProfileRepository) : ProfileUseCase {
    override fun getUserProfile(): Flow<Resource<User>> = repository.getProfile()

    override suspend fun logout() = repository.logout()
}