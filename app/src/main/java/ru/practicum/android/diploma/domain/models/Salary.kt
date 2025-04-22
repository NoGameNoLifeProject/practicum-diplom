package ru.practicum.android.diploma.domain.models

data class Salary(
    val currency: String? = null,
    val from: Int? = null,
    val gross: Boolean? = null,
    val to: Int? = null
)
