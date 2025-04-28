package ru.practicum.android.diploma.domain.models

sealed class FilterParametersState {
    data class AreaSaved(val areas: List<String>?) : FilterParametersState()
    data class AreaFiltered(val areas: List<Area>) : FilterParametersState()
    data object Error : FilterParametersState()
    data class IndustrySaved(val industries: List<String>?) : FilterParametersState()
    data class IndustryFiltered(val industries: List<Industry>) : FilterParametersState()
    data class Salary(val salary: UInt?) : FilterParametersState()
    data class SalaryFiltered(val salary: List<Vacancy>) : FilterParametersState()
    data class OnlyWithSalary(val isChecked: Boolean?) : FilterParametersState()
    data class OnlyWithSalaryFiltered(val salary: List<Vacancy>) : FilterParametersState()
}
