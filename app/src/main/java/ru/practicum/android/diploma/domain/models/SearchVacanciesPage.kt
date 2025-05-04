package ru.practicum.android.diploma.domain.models

data class SearchVacanciesPage(
    val items: List<Vacancy>,
    val found: Int = 0,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
)
