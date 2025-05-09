package ru.practicum.android.diploma.ui.fav_vacancies.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IFavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavVacanciesViewModel(private val favVacanciesInteractor: IFavVacanciesInteractor) : ViewModel() {
    private val _state = MutableLiveData<ResourceState<List<VacancyDetails>>>()
    val state: LiveData<ResourceState<List<VacancyDetails>>> get() = _state

    private fun renderState(state: ResourceState<List<VacancyDetails>>) {
        _state.postValue(state)
    }

    init {
        getFavVacancies()
    }

    private fun getFavVacancies() {
        renderState(ResourceState.Loading())
        viewModelScope.launch {
            favVacanciesInteractor.getFavorite().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.isNullOrEmpty()) {
                            renderState(ResourceState.Error(ResourceState.ErrorType.Empty))
                        } else {
                            renderState(ResourceState.Content(result.data))
                        }
                    }
                    is Resource.Error -> {
                        renderState(ResourceState.Error(ResourceState.ErrorType.NothingFound))
                    }
                }
            }
        }
    }

}
