package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterState


class FilterStorageRepositoryImpl(
    prefs: SharedPreferences
) : FilterStorageRepository {
    private val storage = prefs

    override fun loadFilters(): FilterState = FilterState(
        location = storage.getString("filter_location", null),
        industry = storage.getString("filter_industry", null),
        salaryFrom = storage.getInt("filter_salary", 0).takeIf { it != 0 },
        hideWithoutSalary = storage.getBoolean("filter_hide_no_salary", false)
    )

    override fun saveFilters(state: FilterState) {
        storage.edit()
            .putString("filter_location", state.location)
            .putString("filter_industry", state.industry)
            .putInt("filter_salary", state.salaryFrom ?: 0)
            .putBoolean("filter_hide_no_salary", state.hideWithoutSalary)
            .apply()
    }

    override fun clearFilters() {
        storage.edit().clear().apply()
    }
}
