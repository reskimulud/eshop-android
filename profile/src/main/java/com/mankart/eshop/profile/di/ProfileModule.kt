package com.mankart.eshop.profile.di

import com.mankart.eshop.core.domain.usecase.profile.ProfileInteractor
import com.mankart.eshop.core.domain.usecase.profile.ProfileUseCase
import com.mankart.eshop.core.domain.usecase.transaction.TransactionInteractor
import com.mankart.eshop.core.domain.usecase.transaction.TransactionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {
    @Binds
    @ViewModelScoped
    abstract fun provideProfileUseCase(profileInterface: ProfileInteractor): ProfileUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideTransactionUseCase(transactionInteractor: TransactionInteractor): TransactionUseCase
}