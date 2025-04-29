package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.SelectIndustriesScreenState
import ru.practicum.android.diploma.domain.models.flatten

class SelectIndustriesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
) : ViewModel() {
    private val _state = MutableLiveData<SelectIndustriesScreenState>(SelectIndustriesScreenState.Loading)
    val state: LiveData<SelectIndustriesScreenState> get() = _state

    init {
        updateIndustries()
    }

    private fun renderState(newState: SelectIndustriesScreenState) {
        _state.postValue(newState)
    }

    private fun updateIndustries() {
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.getIndustries().collect { response ->
                when (response) {
                    is Resource.Error -> {
                        renderState(SelectIndustriesScreenState.Error)
                    }

                    is Resource.Success -> {
                        val flatten = response.data.flatMap {
                            it.flatten()
                        }
                        renderState(SelectIndustriesScreenState.Content(flatten))
                    }
                }
            }
        }
    }

}
