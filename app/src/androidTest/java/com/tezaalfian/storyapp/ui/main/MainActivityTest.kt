package com.tezaalfian.storyapp.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.tezaalfian.storyapp.R
import com.tezaalfian.storyapp.data.remote.retrofit.ApiConfig
import com.tezaalfian.storyapp.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getStoriesSuccess() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
    }

    @Test
    fun getStoriesError() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)
        onView(withText("Oops.. something went wrong."))
            .check(matches(isDisplayed()))
    }
}