package ru.practicum.android.diploma.domain.models

sealed class AreaFilterState {
    data class ContentArea(val area: Area?) : AreaFilterState()
    data class ContentCountry(val area: Area?) : AreaFilterState()
    data object EmptyArea : AreaFilterState()
}
