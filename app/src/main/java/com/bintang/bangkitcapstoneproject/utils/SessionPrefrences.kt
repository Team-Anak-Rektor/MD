package com.bintang.bangkitcapstoneproject.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun setLoginSession(isLoggedIn: Boolean) {
        dataStore.edit {
            it[IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun getLoginSession(): Flow<Boolean> {
        return dataStore.data.map {
            it[IS_LOGGED_IN] ?: false
        }
    }

    suspend fun setPrivateKey(privateKey: String) {
        dataStore.edit {
            it[PRIVATE_KEY] = privateKey
        }
    }

    fun getPrivateKey(): Flow<String> {
        return dataStore.data.map {
            it[PRIVATE_KEY] ?: ""
        }
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
        private val PRIVATE_KEY = stringPreferencesKey("Token")

        @Volatile
        private var INSTANCE: SessionPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
