package com.example.accesswebpageexample.presentation.webpage.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.accesswebpageexample.core.DataState
import com.example.accesswebpageexample.core.base.BaseViewModel
import com.example.accesswebpageexample.domain.webpage.ExtractPageTextUseCase
import com.example.accesswebpageexample.presentation.webpage.model.SelectorPreset
import com.example.accesswebpageexample.presentation.webpage.state.WebPageViewState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WebPageViewModel(
    private val extractPageTextUseCase: ExtractPageTextUseCase,
) : BaseViewModel<WebPageViewState>(WebPageViewState.initial()) {

    private var extractJob: Job? = null

    fun onUrlChanged(url: String) {
        setState { copy(url = url) }
    }

    fun onSelectorChanged(selector: String) {
        setState { copy(selector = selector) }
    }

    fun onPresetSelected(preset: SelectorPreset) {
        setState { copy(selector = preset.selector) }
        extract()
    }

    fun extract() {
        val current: WebPageViewState = state.value
        if (!current.hasInput) return

        extractJob?.cancel()
        setState { copy(isLoading = true) }
        extractJob = viewModelScope.launch {
            try {
                val text: String = extractPageTextUseCase(url = current.url, selector = current.selector)
                setState { copy(text = DataState.Value(text), isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                setState { copy(text = text.toError(e), isLoading = false) }
            }
        }
    }
}
