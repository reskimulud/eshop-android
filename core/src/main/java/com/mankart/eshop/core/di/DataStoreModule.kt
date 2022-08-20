package com.mankart.eshop.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mankart.eshop.core.data.source.local.datastore.PreferenceDataStore
import com.mankart.eshop.core.utils.Constants.DATA_STORE_NAME
import com.mankart.eshop.core.utils.SecurityUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> =
        context.dataStore

    @Provides
    fun provideSecurityUtils(): SecurityUtils = SecurityUtils()

    @Provides
    @Singleton
    fun provideAuthPreference(dataStore: DataStore<Preferences>, security: SecurityUtils) : PreferenceDataStore =
        PreferenceDataStore(dataStore, security)

}