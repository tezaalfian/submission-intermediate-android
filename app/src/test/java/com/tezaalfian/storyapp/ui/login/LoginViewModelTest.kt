package com.tezaalfian.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.MainCoroutineRule
import com.tezaalfian.storyapp.data.remote.response.LoginResponse
import com.tezaalfian.storyapp.data.repository.UserRepository
import com.tezaalfian.storyapp.getOrAwaitValue
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.tezaalfian.storyapp.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyToken = "jksfalkjflkjfaieoivmm"
    private val dummyEmail = "user@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `when login success and Result Success`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)

        `when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedLoginResponse)

        val actualLoginResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLoginResponse)
        Assert.assertTrue(actualLoginResponse is Result.Success)
        Assert.assertSame(dummyLoginResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `when signup failed and Result Error`() {
        val loginResponse = MutableLiveData<Result<LoginResponse>>()
        loginResponse.value = Result.Error("Error")

        `when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(loginResponse)

        val actualLoginResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLoginResponse)
        Assert.assertTrue(actualLoginResponse is Result.Error)
    }

    @Test
    fun `Save token successfully`() = mainCoroutineRule.runBlockingTest {
        loginViewModel.setToken(dummyToken, true)
        Mockito.verify(userRepository).setToken(dummyToken, true)
    }
}