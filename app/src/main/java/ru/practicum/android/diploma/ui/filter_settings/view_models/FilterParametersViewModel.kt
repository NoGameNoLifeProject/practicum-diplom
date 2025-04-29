package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam

class FilterParametersViewModel(private val storage: IStorageInteractor) :
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

    // 1) Создаём MediatorLiveData
    private val _filterParam = MediatorLiveData<SearchVacanciesParam>()
    val filterParam: LiveData<SearchVacanciesParam> get() = _filterParam

    init {
        getPram()
    }

    fun setCountry(country: Area?) {
        _countryLiveData.postValue(country)
        _areaLiveData.postValue(null)
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

        //  Подключаем каждую часть к MediatorLiveData
        listOf(
            _countryLiveData,
            _areaLiveData,
            _industryLiveData,
            _salaryLiveData,
            _onlyWithSalaryLiveData
        ).forEach { source ->
            _filterParam.addSource(source) { rebuildFilterParam() }
        }
        //  Первоначальная сборка
        rebuildFilterParam()
    }

    private fun rebuildFilterParam() {
        //  Собираем полный объект из текущих значений
        val combined = SearchVacanciesParam(
            country = _countryLiveData.value,
            areaIDs = _areaLiveData.value,
            industryIDs = _industryLiveData.value,
            salary = _salaryLiveData.value,
            onlyWithSalary = _onlyWithSalaryLiveData.value
        )
        _filterParam.value = combined
    }

    fun saveParam() {
        val savedParam = filterParam.value
        if (savedParam != null) {
            storage.write(savedParam)
        }
    }

    fun clear() {
        _countryLiveData.value = null
        _areaLiveData.value = null
        _salaryLiveData.value = null
        _onlyWithSalaryLiveData.value = null
        storage.clear()
    }

}
