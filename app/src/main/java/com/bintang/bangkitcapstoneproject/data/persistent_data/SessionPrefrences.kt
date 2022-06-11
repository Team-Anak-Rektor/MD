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

    suspend fun setUserId(userId: String) {
        dataStore.edit {
            it[USER_ID] = userId
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map {
            it[USER_ID] ?: ""
        }
    }

    suspend fun setUserName(userName: String) {
        dataStore.edit {
            it[USER_NAME] = userName
        }
    }

    fun getUserName(): Flow<String> {
        return dataStore.data.map {
            it[USER_NAME] ?: ""
        }
    }

    suspend fun setUserEmail(userEmail: String) {
        dataStore.edit {
            it[USER_EMAIL] = userEmail
        }
    }

    fun getUserEmail(): Flow<String> {
        return dataStore.data.map {
            it[USER_EMAIL] ?: ""
        }
    }

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
        private val PRIVATE_KEY = stringPreferencesKey("Token")
        private val USER_ID = stringPreferencesKey("Id")
        private val USER_NAME = stringPreferencesKey("Name")
        private val USER_EMAIL = stringPreferencesKey("Email")


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
