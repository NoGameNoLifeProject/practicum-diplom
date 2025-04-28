package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.models.FilterParametersState
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam

class FilterParametersViewModel(private val interactor: IVacancyInteractor, private val storage: IStorageRepository) :
    ViewModel() {
    private val stateLiveData = MutableLiveData<FilterParametersState>()
    fun observeState(): LiveData<FilterParametersState> = stateLiveData
    private var country = SearchVacanciesParam().country
    private var area = SearchVacanciesParam().areaIDs
    private var industry = SearchVacanciesParam().industryIDs
    private var salary = SearchVacanciesParam().salary
    private var onlyWithSalary = SearchVacanciesParam().onlyWithSalary

    init {
        getPram()
    }

    private fun renderState(state: FilterParametersState) {
        stateLiveData.postValue(state)
    }

    fun getArea() {
        if (area?.name?.isEmpty() == true) {
            renderState(FilterParametersState.SavedCountry(country))
        }
        renderState(FilterParametersState.SavedArea(area))
    }

    fun getIndustry() {
        renderState(FilterParametersState.SavedIndustry(industry))
    }

    fun getSalary(expression: Int) {
        salary = expression.toUInt()
        renderState(FilterParametersState.SavedSalary(salary))
    }

    fun filterOnlyWithSalary() {
        onlyWithSalary = true
        renderState(FilterParametersState.OnlyWithSalary(onlyWithSalary))
    }

    private fun getPram() {
        val param = storage.read()
        country = param.country
        area = param.areaIDs
        industry = param.industryIDs
        salary = param.salary
        onlyWithSalary = param.onlyWithSalary
    }

    fun saveParam() {
        val searchStateParam = SearchVacanciesParam(
            areaIDs = area,
            industryIDs = industry,
            salary = salary,
            onlyWithSalary = onlyWithSalary
        )
        storage.write(searchStateParam)
    }

    fun clear() {
        storage.clear()
    }

}
