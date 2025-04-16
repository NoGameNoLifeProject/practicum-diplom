package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto
import ru.practicum.android.diploma.domain.api.IFavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.IFavVacanciesRepository

class FavVacanciesInteractorImpl(private val favVacanciesRepository: IFavVacanciesRepository) :
    IFavVacanciesInteractor {
    private var isChecked = false

    override fun getFavorite(): Flow<List<VacancyDetailsDto>> {
        return favVacanciesRepository.getAll()
    }

    override suspend fun addToFavorite(vacancy: VacancyDetailsDto) {
        favVacanciesRepository.add(vacancy)
    }

    override suspend fun deleteFromFavorite(vacancy: VacancyDetailsDto) {
        favVacanciesRepository.delete(vacancy)
    }

    override suspend fun isChecked(vacancyId: String): Boolean {
        getFavorite().collect { vacancyList ->
            val found = vacancyList.find { it.id == vacancyId }
            if (found != null) {
                isChecked = true
            }
        }
        return isChecked
    }
}
