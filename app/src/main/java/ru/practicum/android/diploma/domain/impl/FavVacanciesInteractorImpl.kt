package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.api.IFavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.IFavVacanciesRepository
import ru.practicum.android.diploma.domain.api.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavVacanciesInteractorImpl(private val favVacanciesRepository: IFavVacanciesRepository) :
    IFavVacanciesInteractor {

    override fun getFavorite(): Flow<Resource<List<VacancyDetails>>> {
        return favVacanciesRepository.getAll()
    }

    override suspend fun addToFavorite(vacancy: VacancyDetails) {
        favVacanciesRepository.add(vacancy)
    }

    override suspend fun deleteFromFavorite(vacancy: VacancyDetails) {
        favVacanciesRepository.delete(vacancy)
    }

    override fun isChecked(vacancyId: String): Flow<Boolean> = flow {
        getFavorite().collect { result ->
            when (result) {
                is Resource.Success -> {
                    val found = result.data.find { it.id == vacancyId }
                    emit(found != null)
                }

                is Resource.Error -> {
                    emit(false)
                }
            }
        }
    }
}
