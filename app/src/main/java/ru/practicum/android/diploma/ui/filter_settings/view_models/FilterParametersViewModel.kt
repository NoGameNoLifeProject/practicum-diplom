package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.models.AreaFilterState
import ru.practicum.android.diploma.domain.models.IndustryFilterState
import ru.practicum.android.diploma.domain.models.OnlyWithSalaryFilterState
import ru.practicum.android.diploma.domain.models.SalaryFilterState
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam

class FilterParametersViewModel(private val interactor: IVacancyInteractor, private val storage: IStorageRepository) :
    ViewModel() {
    private val areaLiveData = MutableLiveData<AreaFilterState>()
    fun areaObserveState(): LiveData<AreaFilterState> = areaLiveData
    private val industryLiveData = MutableLiveData<IndustryFilterState>()
    fun industryObserveState(): LiveData<IndustryFilterState> = industryLiveData
    private val salaryLiveData = MutableLiveData<SalaryFilterState>()
    fun salaryObserveState(): LiveData<SalaryFilterState> = salaryLiveData
    private val onlyWithSalaryLiveData = MutableLiveData<OnlyWithSalaryFilterState>()
    fun observeOnlyWithSalary(): LiveData<OnlyWithSalaryFilterState> = onlyWithSalaryLiveData
    private var country = SearchVacanciesParam().country
    private var area = SearchVacanciesParam().areaIDs
    private var industry = SearchVacanciesParam().industryIDs
    private var salary = SearchVacanciesParam().salary
    private var onlyWithSalary = SearchVacanciesParam().onlyWithSalary

    init {
        getPram()
    }

    private fun renderAreaState(state: AreaFilterState) {
        areaLiveData.postValue(state)
    }

    private fun renderIndustryState(state: IndustryFilterState) {
        industryLiveData.postValue(state)
    }

    private fun renderSalaryState(state: SalaryFilterState) {
        salaryLiveData.postValue(state)
    }

    private fun renderOnlyWithSalaryState(state: OnlyWithSalaryFilterState) {
        onlyWithSalaryLiveData.postValue(state)
    }

    fun setArea() {
        if (area?.name?.isEmpty() == true) {
            renderAreaState(AreaFilterState.ContentCountry(country))
        } else if (country?.name?.isEmpty() == true) {
            renderAreaState(AreaFilterState.ContentArea(area))
        } else {
            renderAreaState(AreaFilterState.EmptyArea)
        }
    }

    fun setIndustry() {
        if (industry?.name?.isNotEmpty() == true) {
            renderIndustryState(IndustryFilterState.ContentIndustry(industry))
        } else {
            renderIndustryState(IndustryFilterState.EmptyIndustry)
        }
    }

    fun setSalary(expression: Int) {
        if (expression != 0) {
            salary = expression.toUInt()
            renderSalaryState(SalaryFilterState.ContentSalary(salary))
        } else {
            renderIndustryState(IndustryFilterState.EmptyIndustry)
        }
    }

    fun setOnlyWithSalary(isChecked: Boolean) {
        if (isChecked) {
            onlyWithSalary = true
            renderOnlyWithSalaryState(OnlyWithSalaryFilterState.ContentOnlyWithSalary(onlyWithSalary))
        } else {
            renderOnlyWithSalaryState(OnlyWithSalaryFilterState.EmptyOnlyWithSalary)
        }
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
            country = country,
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
