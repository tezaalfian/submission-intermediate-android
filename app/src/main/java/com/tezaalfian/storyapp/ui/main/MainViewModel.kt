package com.tezaalfian.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tezaalfian.storyapp.data.StoryRepository
import com.tezaalfian.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepo: UserRepository, private val storyRepo: StoryRepository) : ViewModel() {

    fun getToken() : LiveData<String> {
        return userRepo.getToken().asLiveData()
    }

    fun isLogin() : LiveData<Boolean>{
        return userRepo.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }

    fun getStories(token: String) = storyRepo.getStories(token)
}