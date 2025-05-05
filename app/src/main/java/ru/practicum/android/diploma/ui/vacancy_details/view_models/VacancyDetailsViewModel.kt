package ru.practicum.android.diploma.ui.vacancy_details.view_models

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IFavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.NO_INTERNET_ERROR_CODE
import java.net.HttpURLConnection

class VacancyDetailsViewModel(
    private val vacancyInteractor: IVacancyInteractor,
    private val favoriteInteractor: IFavVacanciesInteractor,
) : ViewModel() {

    private val _isFavoriteLiveData = MutableLiveData<Boolean>()
    val isFavoriteLiveData: LiveData<Boolean> get() = _isFavoriteLiveData
    private val _vacancyDetailsState = MutableLiveData<ResourceState<VacancyDetails>>(ResourceState.Loading())
    val vacancyDetailsState: LiveData<ResourceState<VacancyDetails>> get() = _vacancyDetailsState

    fun getVacancyDetails(vacancyId: String, isLocal: Boolean) {
        _vacancyDetailsState.value = ResourceState.Loading()
        viewModelScope.launch {
            if (isLocal) {
                val vacancyList = favoriteInteractor.getFavorite().first()
                getFromDataBase(vacancyList, vacancyId)
            } else {
                vacancyInteractor.getVacancyDetails(vacancyId).collect { result ->
                    getFromSearch(result)
                }
            }
            checkFavorites()
        }
    }

    private fun getFromSearch(result: Resource<VacancyDetails>) {
        when (result) {
            is Resource.Success -> {
                _vacancyDetailsState.value = ResourceState.Content(result.data)
            }

            is Resource.Error -> {
                when (result.errorCode) {
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        _vacancyDetailsState.value = ResourceState.Error(ResourceState.ErrorType.NothingFound)
                    }

                    NO_INTERNET_ERROR_CODE -> {
                        _vacancyDetailsState.value = ResourceState.Error(ResourceState.ErrorType.NoInternet)
                    }

                    else -> {
                        _vacancyDetailsState.value = ResourceState.Error(ResourceState.ErrorType.NetworkError)
                    }
                }
            }
        }
    }

    private fun getFromDataBase(vacancyList: Resource<List<VacancyDetails>>, vacancyId: String) {
        when (vacancyList) {
            is Resource.Success -> {
                val vacancy = vacancyList.data.find { it.id == vacancyId }
                if (vacancy != null) {
                    _vacancyDetailsState.value = ResourceState.Content(vacancy)
                } else {
                    _vacancyDetailsState.value = ResourceState.Error(ResourceState.ErrorType.NothingFound)
                }
            }

            is Resource.Error -> {
                _vacancyDetailsState.value = ResourceState.Error(ResourceState.ErrorType.NetworkError)
            }
        }

    }

    fun shareVacancy(context: Context) {
        val vacancy = getCurrentVacancy() ?: return
        val shareText = vacancy.alternateUrl
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться ссылкой на вакансию"))
    }

    private fun checkFavorites() {
        val vacancy = getCurrentVacancy() ?: return
        viewModelScope.launch {
            favoriteInteractor.isChecked(vacancy.id).collect {
                _isFavoriteLiveData.value = it
            }
        }
    }

    fun onFavoriteClicked() {
        val vacancy = getCurrentVacancy() ?: return
        val isFavorite = _isFavoriteLiveData.value ?: false
        viewModelScope.launch {
            if (isFavorite) {
                favoriteInteractor.deleteFromFavorite(vacancy)
            } else {
                favoriteInteractor.addToFavorite(vacancy)
            }
            _isFavoriteLiveData.value = !isFavorite
        }
    }

    private fun getCurrentVacancy(): VacancyDetails? {
        val state = _vacancyDetailsState.value
        return if (state is ResourceState.Content) {
            state.data
        } else {
            null
        }
    }

}
