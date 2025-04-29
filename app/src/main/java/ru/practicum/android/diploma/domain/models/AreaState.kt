package ru.practicum.android.diploma.domain.models

sealed class AreaState {
    data object Loading : AreaState()
    data class ListAreas(val listAreas: List<Area>) : AreaState()
    data object NetworkError : AreaState()
    data object NothingFound : AreaState()
    data object Empty : AreaState()

}
