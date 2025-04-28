package ru.practicum.android.diploma.domain.models

sealed class FilterParametersState {
    data class AreaFiltered(val areas: List<Area>) : FilterParametersState()
    data object Error : FilterParametersState()
    data class IndustryFiltered(val industries: List<Industry>) : FilterParametersState()
    data class SalaryFiltered(val salary: List<Vacancy>) : FilterParametersState()
    data class OnlyWithSalaryFiltered(val salary: List<Vacancy>) : FilterParametersState()
}
