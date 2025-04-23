package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.api.IVacancyRepository
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.ReceivedVacanciesData
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyInteractorImpl(private val repository: IVacancyRepository) : IVacancyInteractor {
    override fun searchVacancies(expression: String): Flow<Resource<ReceivedVacanciesData>> {
        return repository.searchVacancies(expression)
    }

    override fun getCountries(): Flow<Resource<List<Area>>> {
        return repository.getCountries()
    }

    override fun getIndustries(): Flow<Resource<List<Industry>>> {
        return repository.getIndustries()
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<VacancyDetails>> {
        return repository.getVacancyDetails(vacancyId)
    }

    override fun loadNewVacanciesPage(): Flow<Resource<ReceivedVacanciesData>> {
        return repository.loadNewVacanciesPage()
    }
}
