package ru.practicum.android.diploma.domain.models

sealed class SelectIndustriesScreenState {
    data object Loading : SelectIndustriesScreenState()
    data object Error : SelectIndustriesScreenState()
    data class Content(val industries: List<Industry>) : SelectIndustriesScreenState()
}
