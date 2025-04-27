package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterState

interface FilterStorageRepository {
    fun loadFilters(): FilterState
    fun saveFilters(state: FilterState)
    fun clearFilters()
}
