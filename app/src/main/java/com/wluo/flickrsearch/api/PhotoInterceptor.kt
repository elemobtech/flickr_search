package com.wluo.flickrsearch.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Request

private const val API_KEY = "96358825614a5d3b1a1c3fd87fca2b47"

class PhotoInterceptor: Interceptor {
    var page = 1
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestUrl: HttpUrl = originalRequest.url().newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s")
            .addQueryParameter("safesearch", "1")
            .addQueryParameter("page", page.toString())
            .build()

        val request: Request = originalRequest.newBuilder()
            .url(requestUrl)
            .build()

        return chain.proceed(request)
    }
}