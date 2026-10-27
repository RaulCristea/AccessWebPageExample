package com.example.accesswebpageexample.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.accesswebpageexample.data.webpage.HtmlTextExtractor
import com.example.accesswebpageexample.data.webpage.WebPageRemoteDataSource
import com.example.accesswebpageexample.data.webpage.WebPageRepositoryImpl
import com.example.accesswebpageexample.domain.webpage.ExtractPageTextUseCase
import com.example.accesswebpageexample.domain.webpage.WebPageRepository
import com.example.accesswebpageexample.networking.HttpClientFactory
import com.example.accesswebpageexample.presentation.webpage.viewmodel.WebPageViewModel
import io.ktor.client.HttpClient

object SharedModule {

    private val httpClient: HttpClient by lazy { HttpClientFactory.create() }

    private val webPageRepository: WebPageRepository by lazy {
        WebPageRepositoryImpl(
            remoteDataSource = WebPageRemoteDataSource(httpClient = httpClient),
            htmlTextExtractor = HtmlTextExtractor(),
        )
    }

    val webPageViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            WebPageViewModel(extractPageTextUseCase = ExtractPageTextUseCase(webPageRepository = webPageRepository))
        }
    }
}
