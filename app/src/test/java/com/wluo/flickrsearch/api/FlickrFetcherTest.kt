package com.wluo.flickrsearch.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wluo.flickrsearch.MockResponseFileReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection

class FlickrFetcherTest{
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var flickrFetcher: FlickrFetcher

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockWebServer = MockWebServer()
        mockWebServer.start()
        flickrFetcher = FlickrFetcher(mockWebServer.url("").toString())
    }

    @Test
    fun `fetch list and check data returned`(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)
        flickrFetcher.fetchPhotos(1)
        await untilNotNull {flickrFetcher.responseLiveData.value}
        assert(flickrFetcher.responseLiveData.value?.size ?: false == 10)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
