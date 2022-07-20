package com.wluo.flickrsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wluo.flickrsearch.api.FlickrFetcher
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.request.CancelRequestRepository

class PhotoGalleryViewModel(var queryText: String, var urlString: String = "https://api.flickr.com/"): ViewModel() {
    private val cancelRequestRepositoryClass = CancelRequestRepository()
    private var flickrFetcher = FlickrFetcher(urlString)
    var page: Int = 1
    private var mutableItemList: MutableLiveData<ArrayList<GalleryItem>> = flickrFetcher.responseLiveData
    val galleryItemLiveData: LiveData<ArrayList<GalleryItem>>
    get() = mutableItemList

    init {
        fetchPhotos()
    }

    fun fetchPhotos() {
        if (queryText.isBlank()) {
            flickrFetcher.fetchPhotos(page)
        } else {
            flickrFetcher.searchPhotos(queryText, page)
        }
    }

    fun fetchNewPage() {
        if (queryText.isBlank()) {
            flickrFetcher.fetchPhotos(page)
        } else {
            flickrFetcher.searchPhotos(queryText, page)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelRequestRepositoryClass.cancelRequestInFlight()
    }
}