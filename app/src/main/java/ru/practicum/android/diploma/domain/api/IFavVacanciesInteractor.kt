package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface IFavVacanciesInteractor {
    fun getFavorite(): Flow<Resource<List<VacancyDetails>>>
    suspend fun addToFavorite(vacancy: VacancyDetails)
    suspend fun deleteFromFavorite(vacancy: VacancyDetails)
    fun isChecked(vacancyId: String): Flow<Boolean>
}
