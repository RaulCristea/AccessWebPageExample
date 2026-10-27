package com.example.accesswebpageexample.presentation.webpage

import com.example.accesswebpageexample.core.DataState
import com.example.accesswebpageexample.domain.webpage.ExtractPageTextUseCase
import com.example.accesswebpageexample.domain.webpage.SelectorNotMatchedException
import com.example.accesswebpageexample.domain.webpage.WebPageRepository
import com.example.accesswebpageexample.presentation.webpage.model.SelectorPreset
import com.example.accesswebpageexample.presentation.webpage.state.WebPageViewState
import com.example.accesswebpageexample.presentation.webpage.viewmodel.WebPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WebPageViewModelTest {

    private val dispatcher: TestDispatcher = StandardTestDispatcher()
    private val repository: FakeWebPageRepository = FakeWebPageRepository()
    private lateinit var viewModel: WebPageViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = WebPageViewModel(extractPageTextUseCase = ExtractPageTextUseCase(webPageRepository = repository))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun startsWithDefaults() {
        assertEquals(WebPageViewState.initial(), viewModel.state.value)
    }

    @Test
    fun emitsExtractedText() = runTest(dispatcher) {
        repository.result = { "Google" }

        viewModel.extract()
        assertTrue(viewModel.state.value.isLoading)
        advanceUntilIdle()

        val state: WebPageViewState = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(DataState.Value("Google"), state.text)
    }

    @Test
    fun keepsPreviousTextOnError() = runTest(dispatcher) {
        repository.result = { "Google" }
        viewModel.extract()
        advanceUntilIdle()

        repository.result = { throw SelectorNotMatchedException("h1") }
        viewModel.onSelectorChanged("h1")
        viewModel.extract()
        advanceUntilIdle()

        val text: DataState<String> = viewModel.state.value.text
        assertIs<SelectorNotMatchedException>(text.error)
        assertEquals("Google", text.data)
    }

    @Test
    fun ignoresExtractWhenInputIsBlank() = runTest(dispatcher) {
        viewModel.onUrlChanged(" ")
        viewModel.extract()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(DataState.Idle, viewModel.state.value.text)
    }

    @Test
    fun presetSelectionUpdatesSelectorAndExtracts() = runTest(dispatcher) {
        repository.result = { "Heading" }

        viewModel.onPresetSelected(SelectorPreset.HEADINGS)
        advanceUntilIdle()

        val state: WebPageViewState = viewModel.state.value
        assertEquals(SelectorPreset.HEADINGS.selector, state.selector)
        assertEquals(SelectorPreset.HEADINGS, state.selectedPreset)
        assertEquals("h1", repository.lastSelector)
        assertEquals(DataState.Value("Heading"), state.text)
    }

    @Test
    fun customSelectorHasNoPreset() {
        viewModel.onSelectorChanged("div.custom")

        assertEquals(null, viewModel.state.value.selectedPreset)
    }

    private class FakeWebPageRepository : WebPageRepository {
        var result: () -> String = { "" }
        var lastSelector: String? = null

        override suspend fun getText(url: String, selector: String): String {
            lastSelector = selector
            return result()
        }
    }
}
