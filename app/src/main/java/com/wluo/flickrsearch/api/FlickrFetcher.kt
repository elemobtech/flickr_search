package com.wluo.flickrsearch.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.model.PhotoResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrFetcher"

class FlickrFetcher {
    private lateinit var flickrApi: FlickrApi
    val responseLiveData: MutableLiveData<ArrayList<GalleryItem>> = MutableLiveData()
    init {
        updateApi(1)
    }

    private fun updateApi(page: Int) {
        val interceptor = PhotoInterceptor();
        interceptor.page = page
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val gsonDeserializer = GsonBuilder()
            .registerTypeAdapter(PhotoResponse::class.java, PhotoDeserializer())
            .create()
        val gsonConverterFactory = GsonConverterFactory.create(gsonDeserializer)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    private fun fetchPhotosRequest(): Call<PhotoDeserializer> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(page: Int) {
        updateApi(page)
        fetchPhotoMetadata(fetchPhotosRequest())
    }

    private fun searchPhotosRequest(query: String, page: Int): Call<PhotoDeserializer> {
        updateApi(page)
        return flickrApi.searchPhotos(query)
    }

    fun searchPhotos(query: String, page: Int) {
        fetchPhotoMetadata(searchPhotosRequest(query, page))
    }

    private fun fetchPhotoMetadata(flickrRequest: Call<PhotoDeserializer>) {
        flickrRequest.enqueue(object : Callback<PhotoDeserializer> {
            override fun onFailure(call: Call<PhotoDeserializer>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(
                call: Call<PhotoDeserializer>,
                response: Response<PhotoDeserializer>)
            {
                Log.d(TAG,"Response received $response")
                val flickrResponse: PhotoDeserializer? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                val galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                responseLiveData.value = ArrayList(galleryItems)
            }
        })
    }
}