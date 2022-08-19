package com.mankart.eshop.auth.di

import com.mankart.eshop.core.domain.usecase.authentication.AuthenticationInteractor
import com.mankart.eshop.core.domain.usecase.authentication.AuthenticationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthenticationModule {
    @Binds
    @ViewModelScoped
    abstract fun provideAuthenticationUseCase(authenticationInteractor: AuthenticationInteractor): AuthenticationUseCase
}