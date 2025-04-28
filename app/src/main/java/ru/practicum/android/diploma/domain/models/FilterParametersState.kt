package ru.practicum.android.diploma.domain.models

sealed class FilterParametersState {
    data class SavedCountry(val area: String?) : FilterParametersState()
    data class SavedArea(val area: Area?) : FilterParametersState()
    data class SavedIndustry(val industries: MutableList<Industry>?) : FilterParametersState()
    data class SavedSalary(val salary: UInt?) : FilterParametersState()
    data class OnlyWithSalary(val salary: Boolean?) : FilterParametersState()
}
