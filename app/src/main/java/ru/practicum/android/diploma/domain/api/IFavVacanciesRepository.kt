package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto

interface IFavVacanciesRepository {
    suspend fun add(vacancy: VacancyDetailsDto)
    suspend fun delete(vacancy: VacancyDetailsDto)
    fun getAll(): Flow<List<VacancyDetailsDto>>
}
