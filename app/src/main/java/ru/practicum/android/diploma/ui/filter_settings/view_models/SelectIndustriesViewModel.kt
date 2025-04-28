package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Industry

class SelectIndustriesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
    private val storageInteractor: IStorageInteractor
) : ViewModel() {

    init {
        updateIndustries()
    }

    var industries: List<Industry> = listOf()

    fun updateIndustries() {
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.getIndustries().collect { response ->
                when (response) {
                    is Resource.Error -> {}
                    is Resource.Success -> TODO()
                }
            }
        }
    }

}
