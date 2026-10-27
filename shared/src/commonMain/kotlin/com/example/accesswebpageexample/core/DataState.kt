package com.example.accesswebpageexample.core

sealed class DataState<out T> {
    open val data: T? get() = null
    open val error: Throwable? get() = null

    val isIdle: Boolean get() = this is Idle
    val isData: Boolean get() = this is Value
    val isError: Boolean get() = this is Error

    fun toError(error: Throwable): DataState<T> = Error(error = error, value = data)

    data object Idle : DataState<Nothing>()

    data class Value<out T>(val value: T) : DataState<T>() {
        override val data: T get() = value
    }

    data class Error<out T>(override val error: Throwable, val value: T? = null) : DataState<T>() {
        override val data: T? get() = value
    }
}
