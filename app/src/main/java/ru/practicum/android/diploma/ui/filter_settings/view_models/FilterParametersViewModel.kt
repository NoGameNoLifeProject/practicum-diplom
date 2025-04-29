package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam

class FilterParametersViewModel(private val storage: IStorageRepository) :
    ViewModel() {
    private val _areaLiveData = MutableLiveData<Area?>()
    val areaLiveData: LiveData<Area?> get() = _areaLiveData
    private val _countryLiveData = MutableLiveData<Area?>()
    val countryLiveData: LiveData<Area?> get() = _countryLiveData
    private val _industryLiveData = MutableLiveData<Industry?>()
    val industryLiveData: LiveData<Industry?> get() = _industryLiveData
    private val _salaryLiveData = MutableLiveData<UInt?>()
    val salaryLiveData: LiveData<UInt?> get() = _salaryLiveData
    private val _onlyWithSalaryLiveData = MutableLiveData<Boolean?>()
    val onlyWithSalaryLiveData: LiveData<Boolean?> get() = _onlyWithSalaryLiveData

    init {
        getPram()
    }

    fun setCountry(country: Area?) {
        _countryLiveData.postValue(country)
    }

    fun setArea(area: Area?) {
        _areaLiveData.postValue(area)
    }

    fun setIndustry(industry: Industry?) {
        _industryLiveData.postValue(industry)
    }

    fun setSalary(expression: Int?) {
        _salaryLiveData.postValue(expression?.toUInt())
    }

    fun setOnlyWithSalary(isChecked: Boolean?) {
        _onlyWithSalaryLiveData.postValue(isChecked)
    }

    private fun getPram() {
        val param = storage.read()
        _countryLiveData.postValue(param.country)
        _areaLiveData.postValue(param.areaIDs)
        _industryLiveData.postValue(param.industryIDs)
        _salaryLiveData.postValue(param.salary)
        _onlyWithSalaryLiveData.postValue(param.onlyWithSalary)
    }

    fun saveParam() {
        val searchStateParam = SearchVacanciesParam(
            country = _countryLiveData.value,
            areaIDs = _areaLiveData.value,
            industryIDs = _industryLiveData.value,
            salary = _salaryLiveData.value,
            onlyWithSalary = _onlyWithSalaryLiveData.value
        )
        storage.write(searchStateParam)
    }

    fun clear() {
        storage.clear()
    }

}
