package ru.practicum.android.diploma.domain.models

sealed class ResourceState<T> {
    class Loading<T> : ResourceState<T>()

    data class Content<T>(val data: T) : ResourceState<T>()

    data class Error<T>(val errorType: ErrorType) : ResourceState<T>()

    enum class ErrorType {
        NoInternet, NetworkError, NothingFound, Empty
    }
}
