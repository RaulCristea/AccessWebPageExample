package com.example.accesswebpageexample.data.webpage

import com.example.accesswebpageexample.domain.webpage.SelectorNotMatchedException
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.select.Elements

class HtmlTextExtractor {

    fun extract(html: String, selector: String): String {
        val elements: Elements = Ksoup.parse(html).select(selector)
        val texts: List<String> = elements.eachText()
        if (texts.isEmpty()) {
            throw SelectorNotMatchedException(selector)
        }
        return texts.joinToString(separator = "\n")
    }
}
