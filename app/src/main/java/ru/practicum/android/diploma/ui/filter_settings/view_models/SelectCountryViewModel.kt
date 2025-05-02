package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.AreaState

class SelectCountryViewModel(private val interactor: IVacancyInteractor) : ViewModel() {
    private val _areaState = MutableLiveData<AreaState>()
    val areaState: LiveData<AreaState> get() = _areaState

    fun getCountries() {
        _areaState.postValue(AreaState.Loading)
        viewModelScope.launch {
            interactor.getAreas().collect { res ->
                when (res) {
                    is Resource.Success -> {
                        val countries = res.data.sortedBy { it.id }
                        _areaState.postValue(AreaState.ListAreas(countries))
                    }

                    is Resource.Error -> _areaState.postValue(AreaState.NetworkError)
                }
            }
        }

    }

}
