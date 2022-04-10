package com.tezaalfian.storyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tezaalfian.storyapp.data.StoryRepository
import com.tezaalfian.storyapp.data.UserRepository
import com.tezaalfian.storyapp.ui.login.LoginViewModel
import com.tezaalfian.storyapp.ui.main.MainViewModel
import com.tezaalfian.storyapp.ui.signup.SignupViewModel

class UserViewModelFactory(private val userRepo: UserRepository, private val storyRepo: StoryRepository? = null) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                storyRepo?.let { MainViewModel(userRepo, it) } as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}