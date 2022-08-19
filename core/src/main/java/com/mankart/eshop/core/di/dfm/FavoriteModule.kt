package com.mankart.eshop.core.di.dfm

import com.mankart.eshop.core.domain.usecase.favproduct.FavoriteProductInteractor
import com.mankart.eshop.core.domain.usecase.favproduct.FavoriteProductUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Modul ini digunakan untuk mem-provide favorite useCase
 * untuk fitur Dynamic Feature Module (DFM)
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {
    @Binds
    abstract fun provideFavoriteProductUseCase(favoriteProductInteractor: FavoriteProductInteractor): FavoriteProductUseCase
}