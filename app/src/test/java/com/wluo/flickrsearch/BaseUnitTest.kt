package com.wluo.flickrsearch

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
open class BaseUnitTest {
    lateinit var mMockServerInstance: MockWebServer
    private var mShouldStart = false

    @Before
    open fun setUp() {
        startMockServer(true)
    }

    private fun startMockServer(shouldStart: Boolean) {
        if (shouldStart) {
            mShouldStart = shouldStart
            mMockServerInstance = MockWebServer()
            mMockServerInstance.start()
        }
    }

    private fun stopMockServer() {
        if (mShouldStart) {
            mMockServerInstance.shutdown()
        }
    }

    fun mockNetworkResponseWithFileContent(fileName: String, responseCode: Int) =
        mMockServerInstance.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(getJson(fileName))
        )

    fun getJson(path: String): String {
        val uri = javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    @After
    open fun tearDown() {
        stopMockServer()
    }
}