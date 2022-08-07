package com.mankart.eshop.core.di

import android.content.Context
import androidx.room.Room
import com.mankart.eshop.core.data.source.local.room.product.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    // Product Database
    @Singleton
    @Provides
    fun provideProductDatabase(@ApplicationContext context: Context) : ProductDatabase =
        Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "products.db"
        ).fallbackToDestructiveMigration().build()

    // product dao
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase) = productDatabase.productDao()
    // remote keys dao (paging)
    @Provides
    fun provideRemoteKeysDao(productDatabase: ProductDatabase) = productDatabase.remoteKeysDao()
}