package com.example.accesswebpageexample.data.webpage

import com.example.accesswebpageexample.domain.webpage.SelectorNotMatchedException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HtmlTextExtractorTest {

    private val extractor: HtmlTextExtractor = HtmlTextExtractor()
    private val html: String = """
        <html>
            <head><title>Google</title></head>
            <body>
                <p class="item">First</p>
                <p class="item">Second <b>bold</b></p>
            </body>
        </html>
    """.trimIndent()

    @Test
    fun extractsTitle() {
        assertEquals("Google", extractor.extract(html = html, selector = "title"))
    }

    @Test
    fun joinsMultipleMatchesOnSeparateLines() {
        assertEquals("First\nSecond bold", extractor.extract(html = html, selector = "p.item"))
    }

    @Test
    fun throwsWhenSelectorMatchesNothing() {
        assertFailsWith<SelectorNotMatchedException> {
            extractor.extract(html = html, selector = "h1")
        }
    }
}
