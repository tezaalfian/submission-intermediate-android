package com.tezaalfian.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tezaalfian.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {

    fun setToken(token: String){
        viewModelScope.launch {
            repo.setToken(token)
        }
    }

    fun getToken() : LiveData<String> {
        return repo.getToken().asLiveData()
    }
}