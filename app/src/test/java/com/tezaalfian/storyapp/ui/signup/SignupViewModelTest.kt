package com.tezaalfian.storyapp.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tezaalfian.storyapp.DataDummy
import com.tezaalfian.storyapp.data.remote.response.SignupResponse
import com.tezaalfian.storyapp.data.repository.UserRepository
import com.tezaalfian.storyapp.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.tezaalfian.storyapp.data.Result

@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var signupViewModel: SignupViewModel
    private val dummySignupResponse = DataDummy.generateDummySignupResponse()
    private val dummyName = "User"
    private val dummyEmail = "user@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        signupViewModel = SignupViewModel(userRepository)
    }

    @Test
    fun `when signup success and Result Success`() {
        val expectedSignupResponse = MutableLiveData<Result<SignupResponse>>()
        expectedSignupResponse.value = Result.Success(dummySignupResponse)

        `when`(signupViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedSignupResponse)

        val actualSignupResponse = signupViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignupResponse)
        Assert.assertTrue(actualSignupResponse is Result.Success)
        Assert.assertSame(dummySignupResponse, (actualSignupResponse as Result.Success).data)
    }

    @Test
    fun `when signup failed and Result Error`() {
        val signupResponse = MutableLiveData<Result<SignupResponse>>()
        signupResponse.value = Result.Error("Error")

        `when`(signupViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(signupResponse)

        val actualSignupResponse = signupViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignupResponse)
        Assert.assertTrue(actualSignupResponse is Result.Error)
    }
}