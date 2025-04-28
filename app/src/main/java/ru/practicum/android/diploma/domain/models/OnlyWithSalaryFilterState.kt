package ru.practicum.android.diploma.domain.models

sealed class OnlyWithSalaryFilterState {
    data class ContentOnlyWithSalary(val checked: Boolean?) : OnlyWithSalaryFilterState()
    data object EmptyOnlyWithSalary : OnlyWithSalaryFilterState()
}
