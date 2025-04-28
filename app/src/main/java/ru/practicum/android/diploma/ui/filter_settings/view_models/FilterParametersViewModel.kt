package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterParametersState
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam
import ru.practicum.android.diploma.domain.models.Vacancy

class FilterParametersViewModel(private val interactor: IVacancyInteractor, private val storage: IStorageRepository) :
    ViewModel() {
    private val stateLiveData = MutableLiveData<FilterParametersState>()
    fun observeState(): LiveData<FilterParametersState> = stateLiveData
    private var areaSaved = SearchVacanciesParam().areaIDs
    private var industrySaved = SearchVacanciesParam().industryIDs
    private var salarySaved = SearchVacanciesParam().salary
    private var onlyWithSalarySaved = SearchVacanciesParam().onlyWithSalary
    private val areas = ArrayList<Area>()
    private val industries = ArrayList<Industry>()

    init {
        getPram()
    }

    private fun renderState(state: FilterParametersState) {
        stateLiveData.postValue(state)
    }

    fun filterArea() {
        viewModelScope.launch {
            interactor.getCountries().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        areas.addAll(result.data)
                        val filteredArea = areas.filter { areaSaved?.contains(it.name) == true }
                        renderState(FilterParametersState.AreaFiltered(filteredArea))
                    }

                    is Resource.Error -> renderState(FilterParametersState.Error)
                }
            }
        }
    }

    fun filterIndustry() {
        viewModelScope.launch {
            interactor.getIndustries().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        industries.addAll(result.data)
                        val filteredIndustry = industries.filter { industrySaved?.contains(it.name) == true }
                        renderState(FilterParametersState.IndustryFiltered(filteredIndustry))
                    }

                    is Resource.Error -> renderState(FilterParametersState.Error)
                }
            }
        }
    }

    fun filterSalary(vacancy: List<Vacancy>, expression: Int) {
        salarySaved = expression.toUInt()
        val filteredSalary = vacancy.filter { it.salary?.to!! >= expression && it.salary.from!! <= expression }
        renderState(FilterParametersState.SalaryFiltered(filteredSalary))
    }

    fun filterOnlyWithSalary(vacancy: List<Vacancy>) {
        onlyWithSalarySaved = true
        val filteredSalary = vacancy.filter { it.salary?.gross == true }
        renderState(FilterParametersState.OnlyWithSalaryFiltered(filteredSalary))
    }

    private fun getPram() {
        val param = storage.read()
        areaSaved = param.areaIDs
        industrySaved = param.industryIDs
        salarySaved = param.salary
        onlyWithSalarySaved = param.onlyWithSalary
    }

    fun saveParam() {
        val searchStateParam = SearchVacanciesParam(
            areaIDs = areaSaved,
            industryIDs = industrySaved,
            salary = salarySaved,
            onlyWithSalary = onlyWithSalarySaved
        )
        storage.write(searchStateParam)
    }

    fun clear() {
        storage.clear()
    }

}
