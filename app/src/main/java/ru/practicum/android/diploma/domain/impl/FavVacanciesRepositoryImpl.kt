package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.data.mapper.VacancyEntityMapper
import ru.practicum.android.diploma.domain.api.IFavVacanciesRepository

class FavVacanciesRepositoryImpl(private val dataBase: AppDatabase, private val mapper: VacancyEntityMapper) :
    IFavVacanciesRepository {
    override suspend fun add(vacancy: VacancyDetailsDto) {
        dataBase.favVacanciesDao().insertVacancy(mapper.convertFromVacancyDetails(vacancy))
    }

    override suspend fun delete(vacancy: VacancyDetailsDto) {
        dataBase.favVacanciesDao().deleteVacancy(mapper.convertFromVacancyDetails(vacancy))
    }

    override fun getAll(): Flow<List<VacancyDetailsDto>> = flow {
        val vacancies = dataBase.favVacanciesDao().getAllVacancies()
        emit(vacancies.map { mapper.convertToVacancyDetails(it) })
    }
}
