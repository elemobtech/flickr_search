package com.wluo.flickrsearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wluo.flickrsearch.api.FlickrFetcher
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.request.CancelRequestRepository
import com.wluo.flickrsearch.storage.QueryPreferences

class PhotoGalleryViewModel(var queryText: String): ViewModel() {
    private val cancelRequestRepositoryClass = CancelRequestRepository()
    private val flickrFetchr = FlickrFetcher()
    var page: Int = 1
    private var mutableItemList: MutableLiveData<ArrayList<GalleryItem>> = flickrFetchr.responseLiveData
    val galleryItemLiveData: LiveData<ArrayList<GalleryItem>>
    get() = mutableItemList

    init {
        fetchPhotos()
    }

    fun fetchPhotos() {
        if (queryText.isBlank()) {
            flickrFetchr.fetchPhotos(page)
        } else {
            flickrFetchr.searchPhotos(queryText, page)
        }
    }

    fun fetchNewPage() {
        if (queryText.isBlank()) {
            flickrFetchr.fetchPhotos(page)
        } else {
            flickrFetchr.searchPhotos(queryText, page)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelRequestRepositoryClass.cancelRequestInFlight()
    }
}