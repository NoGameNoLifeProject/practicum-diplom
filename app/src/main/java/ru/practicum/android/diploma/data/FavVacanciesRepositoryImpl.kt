package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.FavVacancyEntity
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
    override fun getAll(): Flow<Resource<List<VacancyDetails>>> {
        return dataBase.favVacanciesDao().getAllVacancies()
            .map<List<FavVacancyEntity>, Resource<List<VacancyDetails>>> { entities ->
                Resource.Success(
                    entities.map { entity ->
                        mapper.convertToVacancyDetails(entity)
                    }
                )
            }.catch {
                emit(
                    Resource.Error(HttpURLConnection.HTTP_INTERNAL_ERROR)
                )
            }
    }
}
