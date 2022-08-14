package com.mankart.eshop.core.di.dfm

import com.mankart.eshop.core.domain.usecase.favproduct.FavoriteProductUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {
    fun favoriteProductUseCase(): FavoriteProductUseCase
}
