package ru.practicum.android.diploma.domain.models

data class SearchVacanciesParam(
    val country: String? = null,
    val areaIDs: Area? = null,
    val industryIDs: MutableList<Industry>? = null,
    val salary: UInt? = null,
    val onlyWithSalary: Boolean? = null,
    val page: Int = 0,
    val perPage: Int = 20
)
