package com.wluo.flickrsearch.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class GalleryItem(
    @SerializedName("id") val id: String,
    @ColumnInfo(name = "owner")
    @SerializedName("owner") val owner: String,
    @ColumnInfo(name = "secret")
    @SerializedName("secret") val secret: String,
    @ColumnInfo(name = "server")
    @SerializedName("server") val server: String,
    @ColumnInfo(name = "farm")
    @SerializedName("farm") val farm: Int,
    @ColumnInfo(name = "title")
    @SerializedName("title") val title: String,
    @ColumnInfo(name = "ispublic")
    @SerializedName("ispublic") val ispublic: Int,
    @ColumnInfo(name = "isfriend")
    @SerializedName("isfriend") val isfriend: Int,
    @ColumnInfo(name = "isfamily")
    @SerializedName("isfamily") val isfamily: Int
) {
}
