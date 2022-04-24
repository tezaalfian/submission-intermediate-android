package com.tezaalfian.storyapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tezaalfian.storyapp.data.Result
import com.tezaalfian.storyapp.data.remote.response.LoginResponse
import com.tezaalfian.storyapp.data.remote.response.SignupResponse
import com.tezaalfian.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.lang.Exception

class UserRepository private constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
){

    fun register(name: String, email: String, password: String) : LiveData<Result<SignupResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        }catch (throwable: HttpException){
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Result.Error(it.toString()))
                }
            } catch (exception: Exception) {
                emit(Result.Error(exception.message.toString()))
            }
        }
    }

    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        }catch (throwable: HttpException){
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Result.Error(it.toString()))
                }
            } catch (exception: Exception) {
                emit(Result.Error(exception.message.toString()))
            }
        }
    }

    fun getToken() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    fun isLogin() : Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(
            dataStore: DataStore<Preferences>,
            apiService: ApiService
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}