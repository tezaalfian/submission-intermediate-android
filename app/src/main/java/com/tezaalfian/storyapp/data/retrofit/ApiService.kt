package com.tezaalfian.storyapp.data.retrofit

import com.tezaalfian.storyapp.data.UserRegister
import com.tezaalfian.storyapp.data.response.FileUploadResponse
import com.tezaalfian.storyapp.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("stories/guest")
    fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>
}