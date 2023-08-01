package jti.jasminsa.storyapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jti.jasminsa.storyapp.data.response.LoginResponse
import jti.jasminsa.storyapp.data.response.LoginResult
import jti.jasminsa.storyapp.view.register.RegisterViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<LoginResult> {
        return dataStore.data.map { preferences ->
            Log.e("TAGa", "wrong 1: ${preferences[TOKEN_KEY]}, ${preferences[NAME_KEY]}, ${preferences[USERID_KEY]}")
            LoginResult(
                preferences[TOKEN_KEY] ?:"",
                preferences[NAME_KEY] ?:"",
                preferences[USERID_KEY] ?:"",

            )
        }
    }

    suspend fun saveUser(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[USERID_KEY] = user.userId
            preferences[TOKEN_KEY] = user.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}