package com.tezaalfian.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tezaalfian.storyapp.data.StoryRepository
import com.tezaalfian.storyapp.data.UserRepository
import com.tezaalfian.storyapp.di.StoryInjection
import com.tezaalfian.storyapp.di.UserInjection
import com.tezaalfian.storyapp.ui.main.MainViewModel

class StoryViewModelFactory private constructor(private val userRepo: UserRepository, private val storyRepo: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepo, storyRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(UserInjection.provideRepository(context), StoryInjection.provideRepository(context))
            }.also { instance = it }
    }
}