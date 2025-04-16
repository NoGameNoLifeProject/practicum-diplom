package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.api.IStorageRepository

class StorageRepositoryImpl(private val storageSharedPreferences: SharedPreferences) :
    IStorageRepository {

    override fun read(): List<VacancyDto> {
        val json = storageSharedPreferences.getString(STORAGE_PREFERENCES_KEY, null)
            ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<VacancyDto>::class.java).toList())
    }

    override fun write(vacancies: List<VacancyDto>) {
        val json = Gson().toJson(vacancies)
        storageSharedPreferences.edit().putString(STORAGE_PREFERENCES_KEY, json).apply()
    }

    companion object {
        const val STORAGE_PREFERENCES = "storage_preferences"
        const val STORAGE_PREFERENCES_KEY = "key_for_storage_preferences"
    }
}
