package ru.practicum.android.diploma.domain.models

sealed class SalaryFilterState {
    data class ContentSalary(val salary: UInt?) : SalaryFilterState()
    data object EmptySalary : SalaryFilterState()
}
