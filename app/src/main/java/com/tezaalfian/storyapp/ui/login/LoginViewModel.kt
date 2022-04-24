package com.tezaalfian.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tezaalfian.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {

    fun setToken(token: String, isLogin: Boolean){
        viewModelScope.launch {
            repo.setToken(token, isLogin)
        }
    }

    fun login(email: String, password: String) = repo.login(email, password)
}