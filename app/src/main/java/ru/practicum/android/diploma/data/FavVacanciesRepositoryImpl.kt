package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.mapper.VacancyEntityMapper
import ru.practicum.android.diploma.domain.api.IFavVacanciesRepository
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetails
import java.net.HttpURLConnection

class FavVacanciesRepositoryImpl(val dataBase: AppDatabase, val mapper: VacancyEntityMapper) : IFavVacanciesRepository {
    override suspend fun add(vacancy: VacancyDetails) {
        dataBase.favVacanciesDao().insertVacancy(mapper.convertFromVacancyDetails(vacancy))
    }

    override suspend fun delete(vacancy: VacancyDetails) {
        dataBase.favVacanciesDao().deleteVacancy(mapper.convertFromVacancyDetails(vacancy))
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getAll(): Flow<Resource<List<VacancyDetails>>> = flow {
        try {
            val vacancies = dataBase.favVacanciesDao().getAllVacancies()
            emit(Resource.Success(vacancies.map { mapper.convertToVacancyDetails(it) }))
        } catch (e: Exception) {
            emit(Resource.Error(HttpURLConnection.HTTP_INTERNAL_ERROR))
        }
    }
}
