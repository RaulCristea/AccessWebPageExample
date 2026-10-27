package com.example.accesswebpageexample.data.webpage

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WebPageRepositoryImplTest {

    @Test
    fun fetchesPageAndExtractsText() = runTest {
        val repository: WebPageRepositoryImpl = createRepository(
            status = HttpStatusCode.OK,
            body = "<html><head><title>Example</title></head></html>",
        )

        assertEquals("Example", repository.getText(url = "https://example.com", selector = "title"))
    }

    @Test
    fun propagatesHttpErrors() = runTest {
        val repository: WebPageRepositoryImpl = createRepository(status = HttpStatusCode.NotFound, body = "")

        assertFailsWith<ClientRequestException> {
            repository.getText(url = "https://example.com", selector = "title")
        }
    }

    private fun TestScope.createRepository(
        status: HttpStatusCode,
        body: String,
    ): WebPageRepositoryImpl {
        val httpClient: HttpClient = HttpClient(MockEngine { respond(content = body, status = status) }) {
            expectSuccess = true
        }
        return WebPageRepositoryImpl(
            remoteDataSource = WebPageRemoteDataSource(httpClient = httpClient),
            htmlTextExtractor = HtmlTextExtractor(),
            parsingDispatcher = StandardTestDispatcher(testScheduler),
        )
    }
}
