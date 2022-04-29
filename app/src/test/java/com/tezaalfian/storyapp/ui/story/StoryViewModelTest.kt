package com.tezaalfian.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.data.Result
import com.tezaalfian.storyapp.data.remote.response.UploadStoryResponse
import com.tezaalfian.storyapp.data.repository.StoryRepository
import com.tezaalfian.storyapp.data.repository.UserRepository
import com.tezaalfian.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyToken = "jksfalkjflkjfaieoivmm"
    private val dummyUploadResponse = DataDummy.generateDummyUploadStoryResponse()
    private val dummyFile = DataDummy.generateDummyMultipartFile()
    private val dummyRequestBody = DataDummy.generateDummyRequestBody()

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `Upload story successfully`() {
        val expectedUploadResponse = MutableLiveData<Result<UploadStoryResponse>>()
        expectedUploadResponse.value = Result.Success(dummyUploadResponse)

        `when`(storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null)).thenReturn(expectedUploadResponse)

        val actualUploadResponse = storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadStory(dummyToken, dummyFile, dummyRequestBody, null, null)
        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is Result.Success)
    }

    @Test
    fun `Upload story failed`() {
        val expectedUploadResponse = MutableLiveData<Result<UploadStoryResponse>>()
        expectedUploadResponse.value = Result.Error("Error")

        `when`(storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null)).thenReturn(expectedUploadResponse)

        val actualUploadResponse = storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadStory(dummyToken, dummyFile, dummyRequestBody, null, null)
        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is Result.Error)
    }
}