package com.example.bankapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bankapp.common.Constants.IS_LOGGED_IN
import com.example.bankapp.common.Constants.LOGGED_IN_EMAIL
import com.example.bankapp.common.Constants.USER_LOGIN_PREF
import com.example.bankapp.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_LOGIN_PREF)

class DataStoreOperationImpl(context: Context) : DataStoreOperations {

    private val dataStore = context.dataStore

    private object PreferencesKey {
        val userLoggedInKey = booleanPreferencesKey(name = IS_LOGGED_IN)
    }

    private object PreferencesKeyEmail {
        val userLoggedEmail = stringPreferencesKey(name = LOGGED_IN_EMAIL)
    }

    override suspend fun saveUserLoginState(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userLoggedInKey] = loggedIn
        }
    }

    override fun readUserLoginState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val loggedInState = preferences[PreferencesKey.userLoggedInKey] ?: false
                loggedInState
            }
    }

    override suspend fun saveUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeyEmail.userLoggedEmail] = email
        }
    }

    override fun readUserEmail(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val loggedInEmail = preferences[PreferencesKeyEmail.userLoggedEmail]
                loggedInEmail.toString()
            }
    }

    override suspend fun clearAllDataStore() {
       dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}