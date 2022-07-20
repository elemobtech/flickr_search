package com.wluo.flickrsearch.request

import com.wluo.flickrsearch.api.PhotoDeserializer
import retrofit2.Call

class CancelRequestRepository {

    private lateinit var webRequest: Call<PhotoDeserializer>

    fun cancelRequestInFlight() {
        if (::webRequest.isInitialized) {
            webRequest.cancel()
        }
    }
}