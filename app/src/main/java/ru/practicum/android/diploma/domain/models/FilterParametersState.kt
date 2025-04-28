package ru.practicum.android.diploma.domain.models

sealed class FilterParametersState {
    data class AreaList(val areas: List<Area>):FilterParametersState()
    data class IndustryList(val industries: List<Industry>): FilterParametersState()
    data class SalaryCheck(val isChecked: Boolean): FilterParametersState()
}
