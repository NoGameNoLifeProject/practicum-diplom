package ru.practicum.android.diploma.domain.models

sealed class IndustryFilterState {
    data class ContentIndustry(val industry: Industry?) : IndustryFilterState()
    data object EmptyIndustry : IndustryFilterState()
}
