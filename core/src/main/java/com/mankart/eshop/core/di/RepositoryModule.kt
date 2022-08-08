package com.mankart.eshop.core.di

import com.mankart.eshop.core.data.EShopRepository
import com.mankart.eshop.core.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthenticationRepository(repository: EShopRepository) : IAuthenticationRepository

    @Binds
    abstract fun bindProfileRepository(repository: EShopRepository) : IProfileRepository

    @Binds
    abstract fun bindProductRepository(repository: EShopRepository) : IProductRepository

    @Binds
    abstract fun bindCartRepository(repository: EShopRepository) : ICartRepository

    @Binds
    abstract fun bindTransactionRepository(repository: EShopRepository) : ITransactionRepository

    @Binds
    abstract fun bindFavouriteProductRepository(repository: EShopRepository) : IFavoriteProductRepository
}