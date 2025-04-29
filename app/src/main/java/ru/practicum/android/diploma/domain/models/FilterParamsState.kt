package ru.practicum.android.diploma.domain.models

data class FilterParamsState(
    val area: Area?,
    val country: Area?,
    val industry: Industry?,
    val salary: UInt?,
    val onlyWithSalary: Boolean?,
    val hasDiffs: Boolean,
    val isEmpty: Boolean,
)
