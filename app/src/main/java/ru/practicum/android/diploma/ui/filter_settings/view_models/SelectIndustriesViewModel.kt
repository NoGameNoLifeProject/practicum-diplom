package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.domain.models.flatten
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE

class SelectIndustriesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
) : ViewModel() {
    private val _state = MutableLiveData<ResourceState<List<Industry>>>(ResourceState.Loading())
    val state: LiveData<ResourceState<List<Industry>>> get() = _state

    private var _selectedIndustry = MutableLiveData<Industry?>(null)
    val selectedIndustry: LiveData<Industry?> get() = _selectedIndustry

    private var industries = listOf<Industry>()
    private var selected: Industry? = null

    init {
        updateIndustries()
    }

    private fun renderState(newState: ResourceState<List<Industry>>) {
        _state.postValue(newState)
    }

    private fun updateIndustries() {
        renderState(ResourceState.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.getIndustries().collect { response ->
                when (response) {
                    is Resource.Error -> {
                        when (response.errorCode) {
                            NO_INTERNET_ERROR_CODE -> renderState(
                                ResourceState.Error(ResourceState.ErrorType.NoInternet)
                            )
                            else -> renderState(ResourceState.Error(ResourceState.ErrorType.NetworkError))
                        }
                    }

                    is Resource.Success -> {
                        val flatten = response.data.flatMap {
                            it.flatten()
                        }
                        industries = flatten
                        renderState(ResourceState.Content(flatten))
                    }
                }
            }
        }
    }

    fun filter(expression: String) {
        val selected = this.selected
        if (industries.isNotEmpty()) {
            val filtered = industries.filter { it.name.contains(expression, true) }
            if (filtered.isEmpty()) {
                renderState(ResourceState.Error(ResourceState.ErrorType.Empty))
                _selectedIndustry.postValue(null)
            } else {
                renderState(ResourceState.Content(filtered))
                val isSelectionFilteredOut = selected != null && filtered.find { it.id == selected.id } == null
                if (isSelectionFilteredOut) {
                    _selectedIndustry.postValue(null)
                } else {
                    _selectedIndustry.postValue(selected)
                }
            }
        }
    }

    fun select(industry: Industry?) {
        selected = industry
        _selectedIndustry.postValue(industry)
    }

}
