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
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.domain.models.SearchVacanciesPage
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE
import ru.practicum.android.diploma.util.SEARCH_VACANCY_ITEMS_PER_PAGE
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
    private val storage: IStorageInteractor
) : ViewModel() {
    private val _state = MutableLiveData<ResourceState<SearchVacanciesPage>>()
    private val _showToast = SingleLiveEvent<Int>()
    private val vacancies = mutableListOf<Vacancy>()
    private var founded = 0
    private var lastSearchExpression: String? = null

    val state: LiveData<ResourceState<SearchVacanciesPage>> get() = _state
    val showToast: LiveData<Int> get() = _showToast

    private fun renderState(state: ResourceState<SearchVacanciesPage>) {
        _state.value = state
    }

    val searchVacancies: (String) -> Unit =
        debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, true, this::onSearchVacancies)

    private fun onSearchVacancies(expression: String) {
        if (expression == lastSearchExpression) return
        if (expression.isEmpty()) {
            renderState(ResourceState.Error(ResourceState.ErrorType.Empty))
            return
        }
        lastSearchExpression = expression
        renderState(ResourceState.Loading())
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

        renderState(ResourceState.Loading())
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
        if (current is ResourceState.Content<SearchVacanciesPage> &&
            (current.data.isLoadingMore || current.data.endReached)) {
            return
        }

        renderState(
            ResourceState.Content(
                SearchVacanciesPage(
                    items = vacancies,
                    found = founded,
                    isLoadingMore = true,
                    endReached = false
                )
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
            return renderState(ResourceState.Error(ResourceState.ErrorType.NothingFound))
        }

        val endReached = data.items.size < SEARCH_VACANCY_ITEMS_PER_PAGE || data.page!! >= data.pages!!
        vacancies.addAll(data.items)

        renderState(
            ResourceState.Content(
                SearchVacanciesPage(
                    items = vacancies,
                    found = founded,
                    isLoadingMore = false,
                    endReached = endReached
                )
            )
        )
    }

    private fun processFoundVacanciesError(result: Resource.Error<ReceivedVacanciesData>) {
        val isOnLoadingMore = _state.value is ResourceState.Content

        when (result.errorCode) {
            NO_INTERNET_ERROR_CODE -> {
                if (isOnLoadingMore) {
                    _showToast.value = R.string.search_vacancies_no_internet
                } else {
                    renderState(ResourceState.Error(ResourceState.ErrorType.NoInternet))
                }
            }

            else -> {
                if (isOnLoadingMore) {
                    _showToast.value = R.string.search_vacancies_placeholder_server_error
                } else {
                    renderState(ResourceState.Error(ResourceState.ErrorType.NetworkError))
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
