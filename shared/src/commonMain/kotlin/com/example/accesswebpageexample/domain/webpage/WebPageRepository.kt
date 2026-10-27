package com.example.accesswebpageexample.domain.webpage

interface WebPageRepository {
    suspend fun getText(url: String, selector: String): String
}
