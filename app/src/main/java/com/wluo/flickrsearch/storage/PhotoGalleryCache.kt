package com.wluo.flickrsearch.storage

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache
import java.lang.Exception

private const val TAG = "PhotoGalleryCache"

class PhotoGalleryCache private constructor() {

    private object HOLDER {
        val INSTANCE = PhotoGalleryCache()
    }

    companion object {
        val instance: PhotoGalleryCache by lazy { HOLDER.INSTANCE }
    }

    private val lru: LruCache<Any, Any> = LruCache(1500)

    fun saveBitmapToCache(key: String, bitmap: Bitmap) {
        try {
            instance.lru.put(key, bitmap)
        } catch (e: Exception) {
            // left intentionally blank
        }
        Log.i(TAG, "Got some images for you: $bitmap")
    }

    fun retrieveBitmapFromCache(key: String): Bitmap? {
        try {
            return instance.lru.get(key) as Bitmap?
        } catch (e: Exception) {
            // left intentionally blank
        }
        return null
    }
}