package ru.practicum.android.diploma.domain.api

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val errorCode: Int) : Resource<T>()
}
