package com.example.accesswebpageexample.data.webpage

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class WebPageRemoteDataSource(private val httpClient: HttpClient) {

    suspend fun fetchHtml(url: String): String {
        return httpClient.get(url).bodyAsText()
    }
}
