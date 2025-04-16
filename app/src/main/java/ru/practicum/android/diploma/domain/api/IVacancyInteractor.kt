package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto

interface IVacancyInteractor {
    fun searchVacancies(expression: String): Flow<Pair<List<VacancyDto>?, String?>>
    fun getCountries(): Flow<Pair<List<AreaDto>?, String?>>
    fun getIndustries(): Flow<Pair<List<IndustryDto>?, String?>>
    fun getVacancyDetails(vacancyId: String): Flow<Pair<VacancyDetailsDto?, String?>>
}
