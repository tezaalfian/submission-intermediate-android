package com.tezaalfian.storyapp.data

import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.data.remote.response.LoginResponse
import com.tezaalfian.storyapp.data.remote.response.SignupResponse
import com.tezaalfian.storyapp.data.remote.response.StoriesResponse
import com.tezaalfian.storyapp.data.remote.response.UploadStoryResponse
import com.tezaalfian.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummySignupResponse = DataDummy.generateDummySignupResponse()
    private val dummyStories = DataDummy.generateDummyStoriesResponse()
    private val dummyUploadStory = DataDummy.generateDummyUploadStoryResponse()

    override suspend fun register(name: String, email: String, password: String): SignupResponse {
        return dummySignupResponse
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int
    ): StoriesResponse {
        return dummyStories
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): UploadStoryResponse {
        return dummyUploadStory
    }
}