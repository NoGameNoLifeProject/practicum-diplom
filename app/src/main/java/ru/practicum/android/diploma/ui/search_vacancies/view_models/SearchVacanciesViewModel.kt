package ru.practicum.android.diploma.ui.search_vacancies.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.ReceivedVacanciesData
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE
import ru.practicum.android.diploma.util.SEARCH_VACANCY_ITEMS_PER_PAGE
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
    private val storage: IStorageRepository
) : ViewModel() {
    private val _state = MutableLiveData<SearchVacanciesState>()
    private val _showToast = SingleLiveEvent<Int>()
    private val vacancies = mutableListOf<Vacancy>()
    private var founded = 0
    private var lastSearchExpression: String? = null

    private val _filterIcon = MutableLiveData<Int>()
    val filterIcon: LiveData<Int> get() = _filterIcon

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

    fun updateFilterIcon() {
        val params = storage.read()
        val applied = listOf(
            params.areaIDs?.isNotEmpty() == true,
            params.industryIDs?.isNotEmpty() == true,
            params.salary != null,
            params.onlyWithSalary == true
        ).any { it }

        val iconRes = if (applied) {
            R.drawable.ic_filter_on_24px
        } else {
            R.drawable.ic_filter_off_24px
        }

        _filterIcon.value = iconRes
    }

    fun saveFilters(filters: SearchVacanciesParam) {
        storage.write(filters)
        updateFilterIcon()
    }

    fun resetFilters() {
        val defaultFilters = SearchVacanciesParam()
        storage.write(defaultFilters)
        updateFilterIcon()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
