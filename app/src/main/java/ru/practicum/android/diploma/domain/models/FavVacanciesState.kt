package ru.practicum.android.diploma.domain.models

sealed class FavVacanciesState {
    data object Loading : FavVacanciesState()
    data class VacanciesList(val vacancies: List<VacancyDetails>) : FavVacanciesState()
    data object Error : FavVacanciesState()
    data object Empty : FavVacanciesState()
}
