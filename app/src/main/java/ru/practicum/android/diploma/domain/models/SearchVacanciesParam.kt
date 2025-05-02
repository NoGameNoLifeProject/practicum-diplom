package ru.practicum.android.diploma.domain.models

data class SearchVacanciesParam(
    val country: Area? = null,
    val areaIDs: Area? = null,
    val industryIDs: Industry? = null,
    val salary: UInt? = null,
    val onlyWithSalary: Boolean? = null,
    val page: Int = 0,
    val perPage: Int = 20
)
