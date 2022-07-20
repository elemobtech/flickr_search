package com.wluo.flickrsearch.model

import com.google.gson.annotations.SerializedName

data class GalleryItem(var title: String = "",
                       var id: String = "",
                       @SerializedName("url_s") var url: String ="",
                       @SerializedName("owner") var owner: String = ""

) {
}
