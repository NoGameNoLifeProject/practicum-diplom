package ru.practicum.android.diploma.ui.search_vacancies.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.ReceivedVacanciesData
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE
import ru.practicum.android.diploma.util.SEARCH_VACANCY_ITEMS_PER_PAGE
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
    private val storage: IStorageInteractor
) : ViewModel() {
    private val _state = MutableLiveData<SearchVacanciesState>()
    private val _showToast = SingleLiveEvent<Int>()
    private val vacancies = mutableListOf<Vacancy>()
    private var founded = 0
    private var lastSearchExpression: String? = null

    val state: LiveData<SearchVacanciesState> get() = _state
    val showToast: LiveData<Int> get() = _showToast

    private fun renderState(state: SearchVacanciesState) {
        _state.value = state
    }

    val searchVacancies: (String) -> Unit =
        debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, true, this::onSearchVacancies)

    private fun onSearchVacancies(expression: String) {
        if (expression == lastSearchExpression) return
        if (expression.isEmpty()) {
            renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.Empty))
            return
        }
        lastSearchExpression = expression
        renderState(SearchVacanciesState.Loading)
        viewModelScope.launch {
            vacancyInteractor.searchVacancies(expression).collect { result ->
                vacancies.clear()
                founded = 0
                processFoundVacancies(result)
            }
        }
    }

    fun reLastSearch() {
        if (lastSearchExpression.isNullOrEmpty()) {
            return
        }

        renderState(SearchVacanciesState.Loading)
        viewModelScope.launch {
            vacancyInteractor.searchVacancies(lastSearchExpression!!).collect { result ->
                vacancies.clear()
                founded = 0
                processFoundVacancies(result)
            }
        }
    }

    fun clearSearchExpression() {
        lastSearchExpression = null
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
                processFoundVacanciesSuccess(result)
            }

            is Resource.Error -> {
                processFoundVacanciesError(result)
            }
        }
    }

    private fun processFoundVacanciesSuccess(result: Resource.Success<ReceivedVacanciesData>) {
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

    private fun processFoundVacanciesError(result: Resource.Error<ReceivedVacanciesData>) {
        val isOnLoadingMore = _state.value is SearchVacanciesState.Content

        when (result.errorCode) {
            NO_INTERNET_ERROR_CODE -> {
                if (isOnLoadingMore) {
                    _showToast.value = R.string.search_vacancies_no_internet
                } else {
                    renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.NoInternet))
                }
            }

            else -> {
                if (isOnLoadingMore) {
                    _showToast.value = R.string.search_vacancies_placeholder_server_error
                } else {
                    renderState(SearchVacanciesState.Error(SearchVacanciesState.ErrorType.NetworkError))
                }
            }
        }
    }

    fun filterParamIsNotEmpty(): Boolean {
        val param = storage.read()
        return listOf(param.areaIDs, param.industryIDs, param.salary).any { it != null }
            || param.onlyWithSalary == true
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
