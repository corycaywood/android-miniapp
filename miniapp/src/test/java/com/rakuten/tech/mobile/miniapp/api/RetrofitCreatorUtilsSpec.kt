package com.rakuten.tech.mobile.miniapp.api

import com.nhaarman.mockitokotlin2.mock
import com.rakuten.tech.mobile.sdkutils.RasSdkHeaders
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Test

class RetrofitCreatorUtilsSpec private constructor(
    private val mockServer: MockWebServer
) : MockWebServerBaseTest(mockServer) {

    constructor() : this(MockWebServer())

    private val mockRasSdkHeaders: RasSdkHeaders = mock()
    lateinit var baseUrl: String

    @Before
    fun setup() {
        baseUrl = mockServer.url("/").toString()

        mockServer.enqueue(createTestApiResponse())

        When calling mockRasSdkHeaders.asArray() itReturns emptyArray()
    }

    @Test
    fun `should attach the RAS headers to requests`() {
        When calling mockRasSdkHeaders.asArray() itReturns
            arrayOf("ras_header_name" to "ras_header_value")

        createClient()
            .create(TestApi::class.java)
            .fetch()
            .execute()

        mockServer.takeRequest().getHeader("ras_header_name") shouldEqual "ras_header_value"
    }

    @Test
    fun `should parse a JSON response`() {
        mockServer.enqueue(createTestApiResponse(testValue = "test_value"))

        val response = createClient()
            .create(TestApi::class.java)
            .fetch()
            .execute()

        response.body()!!.testKey shouldEqual "test_value"
    }

    private fun createClient() = createRetrofitClient(
        baseUrl = baseUrl,
        headers = mockRasSdkHeaders
    )
}
