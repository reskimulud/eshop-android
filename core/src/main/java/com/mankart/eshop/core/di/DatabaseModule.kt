package com.mankart.eshop.core.di

import android.content.Context
import androidx.room.Room
import com.mankart.eshop.core.BuildConfig
import com.mankart.eshop.core.data.source.local.room.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    // Product Database
    @Singleton
    @Provides
    fun provideProductDatabase(@ApplicationContext context: Context) : ProductDatabase {
        val dbPassphrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.DB_PASSPHRASE.toCharArray())
        val factory = SupportFactory(dbPassphrase)
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            BuildConfig.DB_NAME
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    // product dao
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase) = productDatabase.productDao()
    // remote keys dao (paging)
    @Provides
    fun provideRemoteKeysDao(productDatabase: ProductDatabase) = productDatabase.remoteKeysDao()
    // favourite product dao
    @Provides
    fun provideFavouriteProductDao(productDatabase: ProductDatabase) = productDatabase.favouriteProductDao()

}