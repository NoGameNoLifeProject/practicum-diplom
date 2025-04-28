package ru.practicum.android.diploma.domain.models

sealed class LoadingState<T> {
    class Loading<T> : LoadingState<T>()
    data class Content<T>(val data: T) : LoadingState<T>()
    data class Error<T>(val errorType: ErrorType) : LoadingState<T>()
    enum class ErrorType {
        NoInternet, NetworkError, NothingFound, Empty
    }
}
