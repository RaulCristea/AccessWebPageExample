package com.example.accesswebpageexample.domain.webpage

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExtractPageTextUseCaseTest {

    private val repository: RecordingWebPageRepository = RecordingWebPageRepository()
    private val useCase: ExtractPageTextUseCase = ExtractPageTextUseCase(webPageRepository = repository)

    @Test
    fun addsHttpsWhenSchemeIsMissing() = runTest {
        useCase(url = " google.com ", selector = " title ")

        assertEquals("https://google.com", repository.lastUrl)
        assertEquals("title", repository.lastSelector)
    }

    @Test
    fun keepsExplicitScheme() = runTest {
        useCase(url = "http://example.com", selector = "h1")

        assertEquals("http://example.com", repository.lastUrl)
    }

    private class RecordingWebPageRepository : WebPageRepository {
        var lastUrl: String? = null
        var lastSelector: String? = null

        override suspend fun getText(url: String, selector: String): String {
            lastUrl = url
            lastSelector = selector
            return ""
        }
    }
}
