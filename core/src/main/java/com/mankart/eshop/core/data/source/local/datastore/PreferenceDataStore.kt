package com.mankart.eshop.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mankart.eshop.core.utils.Constants.EMPTY_DATA_STORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserToken(): Flow<String> = dataStore.data.map { it[USER_ACCESS_TOKEN] ?: EMPTY_DATA_STORE }

    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_ACCESS_TOKEN] = token
        }
    }

    fun getUserEmail(): Flow<String> = dataStore.data.map { it[USER_EMAIL] ?: EMPTY_DATA_STORE }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    fun getUserName(): Flow<String> = dataStore.data.map { it[USER_NAME] ?: EMPTY_DATA_STORE }

    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun clearCache() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val USER_ACCESS_TOKEN = stringPreferencesKey("user_access_token")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
    }
}