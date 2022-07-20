package com.wluo.flickrsearch.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.model.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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
        /*val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
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

        flickrApi = retrofit.create(FlickrApi::class.java)*/
    }

    fun updateApi(page: Int) {
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

    fun fetchPhotosRequest(): Call<PhotoDeserializer> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<ArrayList<GalleryItem>> {
        return fetchPhotoMetadata(fetchPhotosRequest())
    }

    fun searchPhotosRequest(query: String, page: Int): Call<PhotoDeserializer> {
        updateApi(page)
        return flickrApi.searchPhotos(query)
    }

    fun searchPhotos(query: String, page: Int): LiveData<ArrayList<GalleryItem>> {
        return fetchPhotoMetadata(searchPhotosRequest(query, page))
    }

    private fun fetchPhotoMetadata(flickrRequest: Call<PhotoDeserializer>): LiveData<ArrayList<GalleryItem>> {
        val responseLiveData: MutableLiveData<ArrayList<GalleryItem>> = MutableLiveData()
        flickrRequest.enqueue(object : Callback<PhotoDeserializer> {
            override fun onFailure(call: Call<PhotoDeserializer>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(
                call: Call<PhotoDeserializer>,
                response: Response<PhotoDeserializer>
            ) {
                Log.d(TAG,"Response received $response")
                val flickrResponse: PhotoDeserializer? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = ArrayList(galleryItems)
            }
        })
        return responseLiveData
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        return response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
    }
}