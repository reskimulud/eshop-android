package com.mankart.eshop.product.di

import com.mankart.eshop.core.domain.usecase.favproduct.FavoriteProductInteractor
import com.mankart.eshop.core.domain.usecase.products.ProductUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductModule {
    @Binds
    @ViewModelScoped
    abstract fun provideProductUseCase(productInteractor: FavoriteProductInteractor): ProductUseCase
}