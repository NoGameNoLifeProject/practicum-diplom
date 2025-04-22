package ru.practicum.android.diploma.domain.models

sealed class VacancyDetailsState {
    data object Loading : VacancyDetailsState()
    data class VacanciesDetails(val vacancy: VacancyDetails) : VacancyDetailsState()
    data object NetworkError : VacancyDetailsState()
    data object NothingFound : VacancyDetailsState()
    data object Empty : VacancyDetailsState()
}
