package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.data.network.dto.GetVacancyDetailsRequest
import ru.practicum.android.diploma.data.network.dto.SearchVacanciesRequest
import ru.practicum.android.diploma.data.network.dto.SearchVacanciesResponse

interface IVacancyRepository {
    fun searchVacancies(req: SearchVacanciesRequest): Flow<Resource<SearchVacanciesResponse>>
    fun getCountries(): Flow<Resource<List<AreaDto>>>
    fun getIndustries(): Flow<Resource<List<IndustryDto>>>
    fun getVacancyDetails(req: GetVacancyDetailsRequest): Flow<Resource<VacancyDetailsDto>>
}
