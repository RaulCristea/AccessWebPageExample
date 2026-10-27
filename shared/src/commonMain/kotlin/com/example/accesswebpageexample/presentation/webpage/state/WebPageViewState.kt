package com.example.accesswebpageexample.presentation.webpage.state

import com.example.accesswebpageexample.core.DataState
import com.example.accesswebpageexample.core.base.ViewState
import com.example.accesswebpageexample.presentation.webpage.model.SelectorPreset

data class WebPageViewState(
    val url: String,
    val selector: String,
    val text: DataState<String>,
    val isLoading: Boolean,
) : ViewState {

    val hasInput: Boolean
        get() = url.isNotBlank() && selector.isNotBlank()

    val canExtract: Boolean
        get() = !isLoading && hasInput

    val presets: List<SelectorPreset>
        get() = SelectorPreset.entries

    val selectedPreset: SelectorPreset?
        get() = SelectorPreset.entries.firstOrNull { preset -> preset.selector == selector.trim() }

    companion object {
        const val DEFAULT_URL: String = "https://google.com"

        fun initial(): WebPageViewState {
            return WebPageViewState(
                url = DEFAULT_URL,
                selector = SelectorPreset.TITLE.selector,
                text = DataState.Idle,
                isLoading = false,
            )
        }
    }
}
