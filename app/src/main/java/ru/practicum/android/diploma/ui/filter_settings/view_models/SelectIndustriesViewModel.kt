package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.LoadingState

class SelectIndustriesViewModel(
    private val vacancyInteractor: IVacancyInteractor,
    private val storageInteractor: IStorageInteractor
) : ViewModel() {

    init {
        updateIndustries()
    }

    var _selectedIndustry = MutableLiveData<LoadingState<Industry>>(LoadingState.Loading())
    val selectedIndustry: LiveData<LoadingState<Industry>> get() = _selectedIndustry

    var _industries: MutableLiveData<LoadingState<List<Industry>>> = MutableLiveData(LoadingState.Loading())
    val industries: LiveData<LoadingState<List<Industry>>> get() = _industries

    private var loadedIndustries: List<Industry> = listOf()

    fun loadSelectedIndustry() {
        viewModelScope.launch(Dispatchers.IO) {
            val industriesParam = storageInteractor.read().industryIDs
            if (!industriesParam.isNullOrEmpty() && loadedIndustries.isNotEmpty()) {
                val selected = loadedIndustries.find { it.id == industriesParam[0] }
                if (selected != null) {
                    _selectedIndustry.postValue(LoadingState.Content(selected))
                }
            } else {
                _selectedIndustry.postValue(LoadingState.Error(LoadingState.ErrorType.Empty))
            }
        }
    }

    fun selectIndustry(industry: Industry) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedIndustry.postValue(LoadingState.Content(industry))

            val params = storageInteractor.read().apply {
                industryIDs = mutableListOf(industry.id)
            }
            storageInteractor.write(params)
        }
    }

    fun updateIndustries() {
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.getIndustries().collect { response ->
                when (response) {
                    is Resource.Error -> {
                        _industries.postValue(LoadingState.Error(LoadingState.ErrorType.NetworkError))
                    }
                    is Resource.Success -> {
                        loadedIndustries = response.data
                        if (loadedIndustries.isEmpty()) {
                            _industries.postValue(LoadingState.Error(LoadingState.ErrorType.Empty))
                        } else {
                            _industries.postValue(LoadingState.Content(loadedIndustries))
                        }
                    }
                }
                loadSelectedIndustry()
            }
        }
    }

}
