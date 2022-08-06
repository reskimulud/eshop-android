package com.mankart.eshop.core.di

import com.mankart.eshop.core.data.EShopRepository
import com.mankart.eshop.core.domain.repository.IAuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthenticationRepository(repository: EShopRepository) : IAuthenticationRepository
}