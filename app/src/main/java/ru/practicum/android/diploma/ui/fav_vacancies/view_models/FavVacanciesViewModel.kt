package ru.practicum.android.diploma.ui.fav_vacancies.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IFavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.FavVacanciesState

class FavVacanciesViewModel(private val favVacanciesInteractor: IFavVacanciesInteractor) : ViewModel() {
    private val _state = MutableLiveData<FavVacanciesState>()
    val state: LiveData<FavVacanciesState> get() = _state

    private fun renderState(state: FavVacanciesState) {
        _state.postValue(state)
    }

    init {
        getFavVacancies()
    }

    private fun getFavVacancies() {
        renderState(FavVacanciesState.Loading)
        viewModelScope.launch {
            favVacanciesInteractor.getFavorite().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data.isNullOrEmpty()) {
                            renderState(FavVacanciesState.Empty)
                        } else {
                            renderState(FavVacanciesState.VacanciesList(result.data))
                        }
                    }
                    is Resource.Error -> {
                        renderState(FavVacanciesState.Error)
                    }
                }
            }
        }
    }

}
