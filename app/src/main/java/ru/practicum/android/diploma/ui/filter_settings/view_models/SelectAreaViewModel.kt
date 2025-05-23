package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE

class SelectAreaViewModel(private val interactor: IVacancyInteractor) : ViewModel() {

    private val _areaState = MutableLiveData<ResourceState<List<Area>>>()
    val areaState: LiveData<ResourceState<List<Area>>> get() = _areaState
    var allAreas: List<Area> = emptyList()

    fun getCountryByArea(
        selected: Area,
    ): Area? {
        val allAreasFlat = allAreas.flatten()
        // Собираем карту для O(1)-доступа по id
        val byId = allAreasFlat
            .mapNotNull { it.id?.let { id -> id to it } }
            .toMap()

        // Поднимаемся вверх по цепочке parentId
        var current: Area? = selected
        while (current?.parentId != null) {
            current = byId[current.parentId]
        }
        // Если parentId == null — это и есть страна
        return current
    }

    private fun List<Area>.flatten(): List<Area> =
        flatMap { listOf(it) + it.areas.flatten() }

    private fun List<Area>.flattenDescendantsOf(countryId: String): List<Area> {
        // находим нужную страну (родитель с parentId == null)
        val country = find { it.id == countryId }
            ?: return emptyList()
        // расплющиваем её область (рекурсивно)
        return country.areas.flatten()
    }

    fun getAreas(countryId: String?) {
        _areaState.postValue(ResourceState.Loading())
        viewModelScope.launch {
            interactor.getAreas().collect { res ->
                when (res) {
                    is Resource.Success -> {
                        if (countryId.isNullOrEmpty()) {
                            val areas = res.data
                            allAreas = areas
                            val flattenAreas = areas.flatten().filter { it.parentId != null }.sortedBy { it.name }
                            _areaState.postValue(ResourceState.Content(flattenAreas))
                        } else {
                            val areas = res.data
                            allAreas = areas
                            val flattenAreas = res.data.flattenDescendantsOf(countryId).sortedBy { it.name }
                            _areaState.postValue(ResourceState.Content(flattenAreas))
                        }
                    }
                    is Resource.Error -> {
                        when (res.errorCode) {
                            NO_INTERNET_ERROR_CODE -> _areaState.postValue(
                                ResourceState.Error(ResourceState.ErrorType.NoInternet)
                            )
                            else -> _areaState.postValue(
                                ResourceState.Error(ResourceState.ErrorType.NetworkError)
                            )
                        }
                    }
                }
            }

        }

    }

}
