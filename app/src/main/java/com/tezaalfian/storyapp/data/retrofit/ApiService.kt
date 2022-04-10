package com.tezaalfian.storyapp.data.retrofit

import com.tezaalfian.storyapp.data.UserRegister
import com.tezaalfian.storyapp.data.response.FileUploadResponse
import com.tezaalfian.storyapp.data.response.LoginResponse
import com.tezaalfian.storyapp.data.response.RegisterResponse
import com.tezaalfian.storyapp.data.response.StoriesResponse
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
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
//        @Query("page") page: Int?,
//        @Query("size") size: Int?
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): FileUploadResponse
}