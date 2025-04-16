package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto

interface IFavVacanciesInteractor {
    fun getFavorite(): Flow<List<VacancyDetailsDto>>
    suspend fun addToFavorite(vacancy: VacancyDetailsDto)
    suspend fun deleteFromFavorite(vacancy: VacancyDetailsDto)
    suspend fun isChecked(vacancyId: String): Boolean
}
