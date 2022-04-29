package com.tezaalfian.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.tezaalfian.storyapp.data.repository.StoryRepository

class MapsViewModel(private val storyRepo: StoryRepository) : ViewModel() {
    fun getStories(token: String) = storyRepo.getStoriesLocation(token)
}