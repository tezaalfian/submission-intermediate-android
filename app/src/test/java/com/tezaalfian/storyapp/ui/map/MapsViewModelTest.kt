package com.tezaalfian.storyapp.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.tezaalfian.storyapp.data.Result
import com.tezaalfian.storyapp.data.remote.response.StoriesResponse
import com.tezaalfian.storyapp.getOrAwaitValue
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyToken = "jksfalkjflkjfaieoivmm"
    private val dummyStories = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when Get maps story Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<StoriesResponse>>()
        expectedStories.value = Result.Success(dummyStories)
        `when`(mapsViewModel.getStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getStories(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesLocation(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Success)
        Assert.assertSame(dummyStories, (actualStories as Result.Success).data)
        Assert.assertEquals(dummyStories.listStory.size, actualStories.data.listStory.size)
    }

    @Test
    fun `when Network error Should Return Error`() {
        val expectedStories = MutableLiveData<Result<StoriesResponse>>()
        expectedStories.value = Result.Error("Error")
        `when`(mapsViewModel.getStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getStories(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesLocation(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Error)
    }
}