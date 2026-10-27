package com.example.accesswebpageexample.ui.webpage

import androidx.annotation.StringRes
import com.example.accesswebpageexample.R
import com.example.accesswebpageexample.presentation.webpage.model.SelectorPreset

@get:StringRes
val SelectorPreset.labelRes: Int
    get() = when (this) {
        SelectorPreset.TITLE -> R.string.selector_preset_title
        SelectorPreset.HEADINGS -> R.string.selector_preset_headings
        SelectorPreset.SUBHEADINGS -> R.string.selector_preset_subheadings
        SelectorPreset.PARAGRAPHS -> R.string.selector_preset_paragraphs
        SelectorPreset.LINKS -> R.string.selector_preset_links
        SelectorPreset.WHOLE_PAGE -> R.string.selector_preset_whole_page
    }
