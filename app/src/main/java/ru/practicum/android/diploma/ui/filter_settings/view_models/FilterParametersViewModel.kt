package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.FilterStorageRepository

data class FilterState(
    val location: String? = null,
    val industry: String? = null,
    val salaryFrom: Int? = null,
    val hideWithoutSalary: Boolean = false
)

class FilterParametersViewModel(
    private val storage: FilterStorageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(storage.loadFilters())
    val state: StateFlow<FilterState> = _state.asStateFlow()

    fun setLocation(loc: String?) = update { copy(location = loc) }
    fun setIndustry(ind: String?) = update { copy(industry = ind) }
    fun setSalary(from: Int?) = update { copy(salaryFrom = from) }
    fun setHideWithoutSalary(hide: Boolean) = update { copy(hideWithoutSalary = hide) }

    fun clearFilters() = update {
        storage.clearFilters()
        FilterState()
    }

    fun saveFilters() = viewModelScope.launch {
        storage.saveFilters(_state.value)
    }

    private fun update(block: FilterState.() -> FilterState) {
        _state.update { it.block() }
    }
}
