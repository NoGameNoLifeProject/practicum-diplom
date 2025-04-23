package ru.practicum.android.diploma.ui.search_vacancies.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.ReceivedVacanciesData
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE
import ru.practicum.android.diploma.util.SEARCH_VACANCY_ITEMS_PER_PAGE
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(private val vacancyInteractor: IVacancyInteractor) : ViewModel() {
    private val _state = MutableLiveData<SearchVacanciesState>()
    private val vacancies = mutableListOf<Vacancy>()
    private var founded = 0

    val state: LiveData<SearchVacanciesState> get() = _state

    private fun renderState(state: SearchVacanciesState) {
        _state.value = state
    }

    val searchVacancies: (String) -> Unit =
        debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, true, this::onSearchVacancies)

    private fun onSearchVacancies(expression: String) {
        if (expression.isEmpty()) {
            renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.Empty))
            return
        }
        renderState(SearchVacanciesState.Loading)
        viewModelScope.launch {
            vacancyInteractor.searchVacancies(expression).collect { result ->
                vacancies.clear()
                founded = 0
                processFoundVacancies(result)
            }
        }
    }

    fun loadNewVacanciesPage() {
        val current = _state.value
        if (current is SearchVacanciesState.Content && (current.isLoadingMore || current.endReached)) return

        renderState(
            SearchVacanciesState.Content(
                items = vacancies,
                founded = founded,
                isLoadingMore = true,
                endReached = false
            )
        )

        viewModelScope.launch {
            vacancyInteractor.loadNewVacanciesPage().collect { result ->
                processFoundVacancies(result)
            }
        }
    }

    private fun processFoundVacancies(result: Resource<ReceivedVacanciesData>) {
        when (result) {
            is Resource.Success -> {
                val data = result.data
                founded = data.found ?: 0

                if (data.items.isEmpty()) {
                    return renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.NothingFound))
                }

                val endReached = data.items.size < SEARCH_VACANCY_ITEMS_PER_PAGE || data.page!! >= data.pages!!

                vacancies.addAll(data.items)

                renderState(
                    SearchVacanciesState.Content(
                        items = vacancies,
                        founded = founded,
                        isLoadingMore = false,
                        endReached = endReached
                    )
                )
            }

            is Resource.Error -> {
                when (result.errorCode) {
                    NO_INTERNET_ERROR_CODE -> {
                        renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.NoInternet))
                    }
                    else -> {
                        renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.NetworkError))
                    }
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
