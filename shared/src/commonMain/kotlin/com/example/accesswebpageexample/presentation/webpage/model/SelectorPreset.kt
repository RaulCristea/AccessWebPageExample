package com.example.accesswebpageexample.presentation.webpage.model

enum class SelectorPreset(val selector: String) {
    TITLE("title"),
    HEADINGS("h1"),
    SUBHEADINGS("h2, h3"),
    PARAGRAPHS("p"),
    LINKS("a"),
    WHOLE_PAGE("body"),
}
