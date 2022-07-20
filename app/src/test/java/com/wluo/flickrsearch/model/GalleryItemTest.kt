package com.wluo.flickrsearch.model

import com.google.common.truth.Truth
import com.wluo.flickrsearch.BaseUnitTest
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GalleryItemTest: BaseUnitTest() {
    @Test
    fun constructor() = runBlocking {
        val model = GalleryItem(
            "39593986652",
            "36739135@N04",
            "0ec416669f",
            "4740",
            5,
            "IMG_5508",
            1,
            0,
            0
        )
        Truth.assertThat(model.id).isEqualTo("39593986652")
        Truth.assertThat(model.owner).isEqualTo("36739135@N04")
        Truth.assertThat(model.secret).isEqualTo("0ec416669f")
        Truth.assertThat(model.server).isEqualTo("4740")
        Truth.assertThat(model.farm).isEqualTo(5)
        Truth.assertThat(model.title).isEqualTo("IMG_5508")
        Truth.assertThat(model.ispublic).isEqualTo(1)
        Truth.assertThat(model.isfriend).isEqualTo(0)
        Truth.assertThat(model.isfamily).isEqualTo(0)
    }
}