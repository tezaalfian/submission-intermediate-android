package com.tezaalfian.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.tezaalfian.storyapp.data.Result
import com.tezaalfian.storyapp.data.StoryRemoteMediator
import com.tezaalfian.storyapp.data.local.entity.Story
import com.tezaalfian.storyapp.data.local.room.StoryDatabase
import com.tezaalfian.storyapp.data.remote.response.UploadStoryResponse
import com.tezaalfian.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase){

    fun getStories(token: String): LiveData<PagingData<Story>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody): LiveData<Result<UploadStoryResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory("Bearer $token",imageMultipart, desc)
            emit(Result.Success(client))
        }catch (e : Exception){
            e.printStackTrace()
            Log.d("StoryRepository", "uploadStory: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }
}