package com.tezaalfian.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.MainCoroutineRule
import com.tezaalfian.storyapp.PagingDataSourceTest
import com.tezaalfian.storyapp.adapter.ListStoryAdapter
import com.tezaalfian.storyapp.data.FakeApiService
import com.tezaalfian.storyapp.data.local.entity.Story
import com.tezaalfian.storyapp.data.local.room.StoryDatabase
import com.tezaalfian.storyapp.data.remote.retrofit.ApiService
import com.tezaalfian.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var mockStoryRepository: StoryRepository
    private lateinit var storyRepository: StoryRepository
    private val dummyToken = "jksfalkjflkjfaieoivmm"
    private val dummyFile = DataDummy.generateDummyMultipartFile()
    private val dummyRequestBody = DataDummy.generateDummyRequestBody()

    @Before
    fun setup() {
        apiService = FakeApiService()
        storyRepository = StoryRepository(apiService, storyDatabase)
    }

    @Test
    fun `when stories location Should Not Null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = DataDummy.generateDummyStoriesResponse()
        val actualResponse = apiService.getStories(dummyToken, location = 1)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse.listStory.size, actualResponse.listStory.size)
    }

    @Test
    fun `Upload story successfully`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = DataDummy.generateDummyUploadStoryResponse()
        val actualResponse = apiService.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null)
        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `Get stories paging - successfully`() = mainCoroutineRule.runBlockingTest {
        val dummyStories = DataDummy.generateDummyStoriesList()

        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = PagingDataSourceTest.snapshot(dummyStories)

        `when`(mockStoryRepository.getStories(dummyToken)).thenReturn(expectedStories)

        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
        val actualStories = mockStoryRepository.getStories(dummyToken).getOrAwaitValue()
        val storyDiffer = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher
        )
        storyDiffer.submitData(actualStories)
        Assert.assertNotNull(storyDiffer.snapshot())
        Assert.assertEquals(dummyStories.size, storyDiffer.snapshot().size)
    }
}