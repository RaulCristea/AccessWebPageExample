package com.example.accesswebpageexample.networking

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders

object HttpClientFactory {
    private const val REQUEST_TIMEOUT_MILLIS: Long = 15_000
    private const val USER_AGENT: String = "Mozilla/5.0 (Mobile) AccessWebPageExample/1.0"

    fun create(): HttpClient = HttpClient {
        expectSuccess = true
        followRedirects = true
        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
        }
        defaultRequest {
            headers.append(HttpHeaders.UserAgent, USER_AGENT)
        }
    }
}
