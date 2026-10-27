package com.example.accesswebpageexample.data.webpage

import com.example.accesswebpageexample.domain.webpage.WebPageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WebPageRepositoryImpl(
    private val remoteDataSource: WebPageRemoteDataSource,
    private val htmlTextExtractor: HtmlTextExtractor,
    private val parsingDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : WebPageRepository {

    override suspend fun getText(url: String, selector: String): String {
        val html: String = remoteDataSource.fetchHtml(url)
        return withContext(parsingDispatcher) {
            htmlTextExtractor.extract(html = html, selector = selector)
        }
    }
}
