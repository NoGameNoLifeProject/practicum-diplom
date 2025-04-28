package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.SearchVacanciesParam

interface IStorageInteractor {
    fun read(): SearchVacanciesParam
    fun write(filterParam: SearchVacanciesParam)
}
