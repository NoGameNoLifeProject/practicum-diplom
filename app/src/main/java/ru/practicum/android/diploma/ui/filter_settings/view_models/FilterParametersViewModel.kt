package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterParamsState
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

    private var filterParamsInitial: SearchVacanciesParam? = null
    private var filterParamsActual: SearchVacanciesParam? = null

    private val _filterParamsState = MutableLiveData<FilterParamsState?>()
    val filterParamsState: LiveData<FilterParamsState?> get() = _filterParamsState

    init {
        getPram()
    }

    fun setCountry(country: Area?) {
        _countryLiveData.postValue(country)
        _areaLiveData.postValue(null)
        filterParamsActual = filterParamsActual?.copy(country = country, areaIDs = null)
        checkFilterParamsState()
    }

    fun setArea(area: Area?) {
        _areaLiveData.postValue(area)
        filterParamsActual = filterParamsActual?.copy(areaIDs = area)
        checkFilterParamsState()
    }

    fun setIndustry(industry: Industry?) {
        _industryLiveData.postValue(industry)
        filterParamsActual = filterParamsActual?.copy(industryIDs = industry)
        checkFilterParamsState()
    }

    fun setSalary(expression: Int?) {
        _salaryLiveData.postValue(expression?.toUInt())
        filterParamsActual = filterParamsActual?.copy(salary = expression?.toUInt())
        checkFilterParamsState()
    }

    fun setOnlyWithSalary(isChecked: Boolean?) {
        _onlyWithSalaryLiveData.postValue(isChecked)
        filterParamsActual = filterParamsActual?.copy(onlyWithSalary = isChecked)
        checkFilterParamsState()
    }

    private fun getPram() {
        val param = storage.read()
        _countryLiveData.postValue(param.country)
        _areaLiveData.postValue(param.areaIDs)
        _industryLiveData.postValue(param.industryIDs)
        _salaryLiveData.postValue(param.salary)
        _onlyWithSalaryLiveData.postValue(param.onlyWithSalary)

        filterParamsInitial = param
        filterParamsActual = param
        checkFilterParamsState()
    }

    @Suppress("detekt.UnnecessaryParentheses", "detekt.CyclomaticComplexMethod")
    private fun checkFilterParamsState() {
        val hasDiffs = filterParamsActual?.salary != filterParamsInitial?.salary ||
            filterParamsActual?.country != filterParamsInitial?.country ||
            filterParamsActual?.areaIDs != filterParamsInitial?.areaIDs ||
            filterParamsActual?.industryIDs != filterParamsInitial?.industryIDs ||
            (filterParamsActual?.onlyWithSalary ?: false) != (filterParamsInitial?.onlyWithSalary ?: false)

        val isEmpty = filterParamsActual?.salary == null &&
            filterParamsActual?.country == null &&
            filterParamsActual?.areaIDs == null &&
            filterParamsActual?.industryIDs == null &&
            (filterParamsActual?.onlyWithSalary == null || filterParamsActual?.onlyWithSalary == false)

        _filterParamsState.postValue(
            FilterParamsState(
                area = filterParamsActual?.areaIDs,
                country = filterParamsActual?.country,
                industry = filterParamsActual?.industryIDs,
                salary = filterParamsActual?.salary,
                onlyWithSalary = filterParamsActual?.onlyWithSalary,
                hasDiffs = hasDiffs,
                isEmpty = isEmpty
            )
        )
    }

    fun saveParam() {
        val savedParam = filterParamsActual
        filterParamsInitial = savedParam
        if (savedParam != null) {
            storage.write(savedParam)
        }
    }

    fun clear() {
        _countryLiveData.value = null
        _areaLiveData.value = null
        _salaryLiveData.value = null
        _industryLiveData.value = null
        _onlyWithSalaryLiveData.value = null
        storage.clear()

        filterParamsActual = SearchVacanciesParam(
            country = _countryLiveData.value,
            areaIDs = _areaLiveData.value,
            industryIDs = _industryLiveData.value,
            salary = _salaryLiveData.value,
            onlyWithSalary = _onlyWithSalaryLiveData.value
        )
        filterParamsInitial = filterParamsActual
        checkFilterParamsState()
    }

}
