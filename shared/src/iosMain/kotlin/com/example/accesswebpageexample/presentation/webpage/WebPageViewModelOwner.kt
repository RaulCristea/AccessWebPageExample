package com.example.accesswebpageexample.presentation.webpage

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.accesswebpageexample.di.SharedModule
import com.example.accesswebpageexample.presentation.webpage.state.WebPageViewState
import com.example.accesswebpageexample.presentation.webpage.viewmodel.WebPageViewModel
import kotlinx.coroutines.launch

class WebPageViewModelOwner : ViewModelStoreOwner {

    override val viewModelStore: ViewModelStore = ViewModelStore()

    val viewModel: WebPageViewModel = ViewModelProvider.create(
        store = viewModelStore,
        factory = SharedModule.webPageViewModelFactory,
    )[WebPageViewModel::class]

    val currentState: WebPageViewState
        get() = viewModel.state.value

    fun observe(onChange: (WebPageViewState) -> Unit) {
        viewModel.viewModelScope.launch {
            viewModel.state.collect(onChange)
        }
    }

    fun clear() {
        viewModelStore.clear()
    }
}
