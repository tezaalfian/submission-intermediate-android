package com.tezaalfian.storyapp.di

import android.content.Context
import com.tezaalfian.storyapp.data.repository.StoryRepository
import com.tezaalfian.storyapp.data.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}