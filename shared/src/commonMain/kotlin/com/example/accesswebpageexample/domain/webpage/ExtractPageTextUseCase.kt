package com.example.accesswebpageexample.domain.webpage

class ExtractPageTextUseCase(private val webPageRepository: WebPageRepository) {

    suspend operator fun invoke(url: String, selector: String): String {
        return webPageRepository.getText(
            url = normalize(url),
            selector = selector.trim(),
        )
    }

    private fun normalize(url: String): String {
        val trimmed: String = url.trim()
        return if (trimmed.contains(SCHEME_SEPARATOR)) trimmed else "$DEFAULT_SCHEME$SCHEME_SEPARATOR$trimmed"
    }

    private companion object {
        const val SCHEME_SEPARATOR: String = "://"
        const val DEFAULT_SCHEME: String = "https"
    }
}
