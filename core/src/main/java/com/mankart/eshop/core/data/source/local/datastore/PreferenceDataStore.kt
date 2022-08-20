package com.mankart.eshop.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mankart.eshop.core.utils.Constants.EMPTY_DATA_STORE
import com.mankart.eshop.core.utils.SecurityUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val security: SecurityUtils
) {
    private val jsonEncode: Json = Json { encodeDefaults = true }

    // ======================================================================== //

    fun getUserToken(): Flow<String> = dataStore.data.secureMap { it[USER_ACCESS_TOKEN] ?: EMPTY_DATA_STORE }

    suspend fun saveUserToken(token: String) {
        dataStore.secureEdit(token) { preferences, encryptedToken ->
            preferences[USER_ACCESS_TOKEN] = encryptedToken
        }
    }

    fun getUserEmail(): Flow<String> = dataStore.data.secureMap { it[USER_EMAIL] ?: EMPTY_DATA_STORE }

    suspend fun saveUserEmail(email: String) {
        dataStore.secureEdit(email) { preferences, encryptedEmail ->
            preferences[USER_EMAIL] = encryptedEmail
        }
    }

    fun getUserName(): Flow<String> = dataStore.data.secureMap { it[USER_NAME] ?: EMPTY_DATA_STORE }

    suspend fun saveUserName(name: String) {
        dataStore.secureEdit(name) { preferences, encryptedName ->
            preferences[USER_NAME] = encryptedName
        }
    }

    suspend fun clearCache() {
        dataStore.edit {
            it.clear()
        }
    }

    // method untuk mengenkripsi data store
    private suspend inline fun <reified T> DataStore<Preferences>.secureEdit(
        value: T,
        crossinline editStore: (MutablePreferences, String) -> Unit
    ) {
        edit {
            val encryptedValue = security.encryptData(Json.encodeToString(value))
            editStore.invoke(it, encryptedValue)
        }
    }

    // method untuk mendekripsi value data store
    private inline fun <reified T> Flow<Preferences>.secureMap(
        crossinline fetchValue: (value: Preferences) -> String,
    ): Flow<T> =
        map { preference ->
            val value = fetchValue(preference)

            if (value.isNotEmpty() && value != EMPTY_DATA_STORE) {
                val decryptedValue = security.decryptData(value)
                jsonEncode.decodeFromString(decryptedValue)
            } else {
                value as T
            }
        }

    companion object {
        private val USER_ACCESS_TOKEN = stringPreferencesKey("user_access_token")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
    }
}