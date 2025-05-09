package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE

class SelectCountryViewModel(private val interactor: IVacancyInteractor) : ViewModel() {
    private val _areaState = MutableLiveData<ResourceState<List<Area>>>()
    val areaState: LiveData<ResourceState<List<Area>>> get() = _areaState

    fun getCountries() {
        _areaState.postValue(ResourceState.Loading())
        viewModelScope.launch {
            interactor.getAreas().collect { res ->
                when (res) {
                    is Resource.Success -> {
                        val countries = res.data.sortedBy { it.id }
                        _areaState.postValue(ResourceState.Content(countries))
                    }

                    is Resource.Error -> {
                        when (res.errorCode) {
                            NO_INTERNET_ERROR_CODE -> _areaState.postValue(
                                ResourceState.Error(ResourceState.ErrorType.NoInternet)
                            )
                            else -> _areaState.postValue(
                                ResourceState.Error(ResourceState.ErrorType.NetworkError)
                            )
                        }
                    }
                }
            }
        }

    }

}
