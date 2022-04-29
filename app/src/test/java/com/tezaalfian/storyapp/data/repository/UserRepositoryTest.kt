package com.tezaalfian.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.MainCoroutineRule
import com.tezaalfian.storyapp.data.FakeApiService
import com.tezaalfian.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var dataStore : DataStore<Preferences>
    private lateinit var userRepository: UserRepository

    private val dummyName = "User"
    private val dummyEmail = "user@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        apiService = FakeApiService()
        userRepository = UserRepository.getInstance(dataStore, apiService)
    }

    @Test
    fun `when login response Should Not Null`() = mainCoroutineRule.runBlockingTest{
        val expectedResponse = DataDummy.generateDummyLoginResponse()
        val actualResponse = apiService.login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse)
    }

    @Test
    fun `when register response Should Not Null`() = mainCoroutineRule.runBlockingTest{
        val expectedResponse = DataDummy.generateDummySignupResponse()
        val actualResponse = apiService.register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse)
    }
}