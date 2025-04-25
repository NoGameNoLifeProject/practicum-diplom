package ru.practicum.android.diploma.domain.models

sealed class SearchVacanciesState {
    data object Loading : SearchVacanciesState()

    data class Content(
        val items: List<Vacancy>,
        val founded: Int = 0,
        val isLoadingMore: Boolean = false,
        val endReached: Boolean = false,
    ) : SearchVacanciesState()

    data class Error(val errorType: ErrorType) : SearchVacanciesState()

    enum class ErrorType {
        NoInternet, NetworkError, NothingFound, Empty
    }
}
