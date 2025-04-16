package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.data.dto.VacancyDto

interface IStorageRepository {
    fun read(): List<VacancyDto>
    fun write(vacancies: List<VacancyDto>)
}
