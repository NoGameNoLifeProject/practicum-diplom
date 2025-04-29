package ru.practicum.android.diploma.ui.filter_settings.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.models.Area

class SelectLocationViewModel : ViewModel() {
    private val _area = MutableLiveData<Area?>()
    val area: LiveData<Area?> get() = _area
    private val _country = MutableLiveData<Area?>()
    val country: LiveData<Area?> get() = _country

    fun setArea(area: Area?) {
        _area.postValue(area)
    }

    fun setCountry(country: Area?) {
        _country.postValue(country)
    }
}
