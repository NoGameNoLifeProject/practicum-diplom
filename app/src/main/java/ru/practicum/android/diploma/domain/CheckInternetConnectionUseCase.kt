package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.data.NetworkInfoDataSource

class CheckInternetConnectionUseCase(
    private val networkInfo: NetworkInfoDataSource
) {
    operator fun invoke(): Boolean = networkInfo.isConnected()
}
