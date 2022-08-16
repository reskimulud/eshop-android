package com.mankart.eshop.core.domain.usecase.profile

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.User
import com.mankart.eshop.core.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileInteractor @Inject constructor(private val repository: IProfileRepository) : ProfileUseCase {
    override fun getUserProfile(): Flow<Resource<User>> = repository.getProfile()

    override fun postPreview(productId: String, rate: Int, review: String): Flow<Resource<String>> =
        repository.postReview(productId, rate, review)

    override suspend fun logout() = repository.logout()

    override suspend fun getName(): Flow<String> = repository.getName()

    override suspend fun getEmail(): Flow<String> = repository.getEmail()

    override suspend fun saveName(name: String) = repository.saveName(name)

    override suspend fun saveEmail(email: String) = repository.saveEmail(email)
}