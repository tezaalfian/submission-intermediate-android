package com.tezaalfian.storyapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tezaalfian.storyapp.data.local.UserPreference
import com.tezaalfian.storyapp.data.response.RegisterResponse
import com.tezaalfian.storyapp.data.retrofit.ApiConfig
import java.lang.Exception

class UserRepository private constructor(private val prefDataStore: UserPreference){

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = ApiConfig.getApiService().register(name, email, password)
            emit(Result.Success(result))
        }catch (e : Exception){
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(pref: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(pref)
                INSTANCE = instance
                instance
            }
        }
    }
}