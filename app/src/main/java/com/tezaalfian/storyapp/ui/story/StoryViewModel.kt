package com.tezaalfian.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tezaalfian.storyapp.data.repository.StoryRepository
import com.tezaalfian.storyapp.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val userRepo: UserRepository, private val storyRepo: StoryRepository) : ViewModel() {

    fun getToken() : LiveData<String> {
        return userRepo.getToken().asLiveData()
    }

    fun uploadStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        desc: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = storyRepo.uploadStory(token, imageMultipart, desc, lat, lon)
}