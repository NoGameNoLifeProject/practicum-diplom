package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.Industry
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.data.network.dto.GetVacancyDetailsRequest
import ru.practicum.android.diploma.data.network.dto.SearchVacanciesRequest
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.IVacancyRepository
import ru.practicum.android.diploma.domain.api.Resource

class VacancyInteractorImpl(private val repository: IVacancyRepository) : IVacancyInteractor {
    override fun searchVacancies(expression: String): Flow<Pair<List<VacancyDto>?, String?>> = flow {
        repository.searchVacancies(SearchVacanciesRequest(text = expression, page = 0)).collect { result ->
            when (result) {
                is Resource.Error -> {
                    emit(Pair(null, result.message))
                }

                is Resource.Success -> {
                    emit(Pair(result.data?.items?.toList(), null))
                }
            }
        }
    }

    override fun getCountries(): Flow<Pair<List<AreaDto>?, String?>> = flow {
        repository.getCountries().collect() { result ->
            when (result) {
                is Resource.Error -> {
                    emit(Pair(null, result.message))
                }

                is Resource.Success -> {
                    Pair(result.data, null)
                }
            }
        }
    }

    override fun getIndustries(): Flow<Pair<List<Industry>?, String?>> = flow {
        repository.getIndustries().collect { result ->
            when (result) {
                is Resource.Error -> {
                    emit(Pair(null, result.message))
                }

                is Resource.Success -> {
                    emit(Pair(result.data, null))
                }
            }
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Pair<VacancyDetailsDto?, String?>> = flow {
        repository.getVacancyDetails(GetVacancyDetailsRequest(vacancyId)).collect { result ->
            when (result) {
                is Resource.Error -> {
                    emit(Pair(null, result.message))
                }

                is Resource.Success -> {
                    if (result.data == null) {
                        emit(Pair(null, result.message))
                    } else {
                        emit(Pair(result.data, null))
                    }
                }
            }
        }
    }
}
