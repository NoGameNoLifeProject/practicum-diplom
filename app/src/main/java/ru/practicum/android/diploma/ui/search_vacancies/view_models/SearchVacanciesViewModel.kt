package ru.practicum.android.diploma.ui.search_vacancies.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(private val vacancyInteractor: IVacancyInteractor) : ViewModel() {
    private val _state = MutableLiveData<SearchVacanciesState>()
    private var isLoadingNewPage = false

    val state: LiveData<SearchVacanciesState> get() = _state

    private fun renderState(state: SearchVacanciesState) {
        _state.value = state
    }

    val searchVacancies: (String) -> Unit =
        debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, true, this::onSearchVacancies)

    private fun onSearchVacancies(expression: String) {
        if (expression.isNotEmpty()) {
            renderState(SearchVacanciesState.Loading)
            viewModelScope.launch {
                vacancyInteractor.searchVacancies(expression).collect { (foundedVacancies, errorMessage) ->
                    vacancies.clear()
                    // TODO: переделать при добавлении проверки интернета
                    processFoundVacancies(foundedVacancies, errorMessage)
                }
            }
        } else {
            renderState(SearchVacanciesState.Empty)
        }
    }

    fun loadNewVacanciesPage() {
        renderState(SearchVacanciesState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.loadNewVacanciesPage().collect { (foundedVacancies, errorMessage) ->
                processFoundVacancies(foundedVacancies, errorMessage)
        if (!isLoadingNewPage) {
            isLoadingNewPage = true
            renderState(SearchVacanciesState.Loading)
            viewModelScope.launch(Dispatchers.IO) {
                vacancyInteractor.loadNewVacanciesPage().collect { (foundedVacancies, errorMessage) ->
                    processFoundVacancies(foundedVacancies, errorMessage)
                    isLoadingNewPage = false
                }
            }
        }
    }

    private fun processFoundVacancies(foundedVacancies: List<Vacancy>?, errorMessage: String?) {
        if (!foundedVacancies.isNullOrEmpty()) {
            vacancies.addAll(foundedVacancies)
            renderState(SearchVacanciesState.VacanciesList(vacancies))
        } else if (foundedVacancies.isNullOrEmpty() && errorMessage != null) {
            renderState(SearchVacanciesState.NetworkError)
        } else {
            renderState(SearchVacanciesState.NothingFound)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
