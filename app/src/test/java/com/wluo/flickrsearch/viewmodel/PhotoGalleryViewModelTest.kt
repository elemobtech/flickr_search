package com.wluo.flickrsearch.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bumptech.glide.load.engine.Resource
import com.wluo.flickrsearch.MockResponseFileReader
import junit.framework.Assert
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
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
        viewModel = PhotoGalleryViewModel("melbourne")

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun `read sample success json file`(){
        val reader = MockResponseFileReader("success_response.json")
        Assert.assertNotNull(reader.content)
    }

    @Test
    fun `fetch details and check response Code 200 returned`(){
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)
        // Act
        viewModel.fetchPhotos()
        Assert.assertNotNull(viewModel.galleryItemLiveData.value)
        assert(viewModel.galleryItemLiveData.value?.size ?: false == 5)
    }
}