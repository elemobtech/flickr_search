package com.wluo.flickrsearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wluo.flickrsearch.MockResponseFileReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilNotNull
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class PhotoGalleryViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var viewModel: PhotoGalleryViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mockWebServer = MockWebServer()
        mockWebServer.start()

        viewModel = PhotoGalleryViewModel("melbourne", mockWebServer.url("").toString())
    }

    @Test
    fun `read sample success json file`(){
        val reader = MockResponseFileReader("success_response.json")
        Assert.assertNotNull(reader.content)
    }

    @Test
    fun `fetch details and check content returned`(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)
        viewModel.fetchPhotos()
        await untilNotNull {viewModel.galleryItemLiveData.value}
        assert(viewModel.galleryItemLiveData.value?.size ?: false == 10)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}