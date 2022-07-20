package com.wluo.flickrsearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wluo.flickrsearch.api.FlickrFetcher
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.request.CancelRequestRepositoryClass
import com.wluo.flickrsearch.storage.QueryPreferences

class PhotoGalleryViewModel(private val app: Application): AndroidViewModel(app) {
    private val cancelRequestRepositoryClass = CancelRequestRepositoryClass()
    private val flickrFetchr = FlickrFetcher()
    private val mutableSearchTerm = MutableLiveData<String>()
    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""
    var galleryItemLiveData: LiveData<ArrayList<GalleryItem>>

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)

        // What we added here will make the ImageResults of our GalleryItem to reflect the latest request or changes of our photoSearch
        galleryItemLiveData =
            Transformations.switchMap(mutableSearchTerm) { searchItem ->
                // this will still update the user with photos even though the search_item has been cleared
                if (searchItem.isBlank()) {
                    flickrFetchr.fetchPhotos()
                } else {
                    flickrFetchr.searchPhotos(searchItem, 1)
                }
            }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }

    fun

    override fun onCleared() {
        super.onCleared()
        cancelRequestRepositoryClass.cancelRequestInFlight()
    }
}