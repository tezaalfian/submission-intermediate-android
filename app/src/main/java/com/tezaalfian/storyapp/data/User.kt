package com.tezaalfian.storyapp.data

import com.google.gson.annotations.SerializedName

data class UserRegister(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class UserLogin(
    val email: String,
    val password: String
)
