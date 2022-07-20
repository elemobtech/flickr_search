package com.wluo.flickrsearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wluo.flickrsearch.api.FlickrFetcher
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.request.CancelRequestRepository
import com.wluo.flickrsearch.storage.QueryPreferences

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {
    private val cancelRequestRepositoryClass = CancelRequestRepository()
    private val flickrFetchr = FlickrFetcher()
    var page: Int = 1
    var searchTerm: String = ""
    private var mutableItemList: MutableLiveData<ArrayList<GalleryItem>> = flickrFetchr.responseLiveData
    val galleryItemLiveData: LiveData<ArrayList<GalleryItem>>
    get() = mutableItemList

    init {
        fetchPhotos(QueryPreferences.getStoredQuery(app))
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        searchTerm = query
        if (query.isBlank()) {
            flickrFetchr.fetchPhotos(page)
        } else {
            flickrFetchr.searchPhotos(query, page)
        }
    }

    fun fetchNewPage() {
        val query = QueryPreferences.getStoredQuery(app)
        if (query.isBlank()) {
            flickrFetchr.fetchPhotos(page)
        } else {
            flickrFetchr.searchPhotos(query, page)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelRequestRepositoryClass.cancelRequestInFlight()
    }
}