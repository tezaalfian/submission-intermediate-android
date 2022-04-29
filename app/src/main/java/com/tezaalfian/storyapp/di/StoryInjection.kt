package com.tezaalfian.storyapp.di

import android.content.Context
import com.tezaalfian.storyapp.data.local.room.StoryDatabase
import com.tezaalfian.storyapp.data.repository.StoryRepository
import com.tezaalfian.storyapp.data.remote.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService, database)
    }
}