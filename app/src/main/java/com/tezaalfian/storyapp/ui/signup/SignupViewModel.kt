package com.tezaalfian.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import com.tezaalfian.storyapp.data.UserRepository

class SignupViewModel(private val repo: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = repo.register(name, email, password)
}