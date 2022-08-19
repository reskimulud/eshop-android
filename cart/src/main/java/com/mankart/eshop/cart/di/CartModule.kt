package com.mankart.eshop.cart.di

import com.mankart.eshop.core.domain.usecase.cart.CartInteractor
import com.mankart.eshop.core.domain.usecase.cart.CartUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CartModule {
    @Binds
    @ViewModelScoped
    abstract fun provideCartUseCase(cartInteractor: CartInteractor): CartUseCase

    // transaction use case di provide oleh modul profile
}