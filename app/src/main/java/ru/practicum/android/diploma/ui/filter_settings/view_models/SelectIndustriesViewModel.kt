package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
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

    init {
        updateIndustries()
    }

    private fun renderState(newState: ResourceState<List<Industry>>) {
        _state.postValue(newState)
    }

    private fun updateIndustries() {
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.getIndustries().collect { response ->
                when (response) {
                    is Resource.Error -> {
                        when (response.errorCode) {
                            NO_INTERNET_ERROR_CODE -> renderState(ResourceState.Error(ResourceState.ErrorType.NoInternet))
                            else -> renderState(ResourceState.Error(ResourceState.ErrorType.NetworkError))
                        }
                    }

                    is Resource.Success -> {
                        val flatten = response.data.flatMap {
                            it.flatten()
                        }
                        renderState(ResourceState.Content(flatten))
                    }
                }
            }
        }
    }

}
