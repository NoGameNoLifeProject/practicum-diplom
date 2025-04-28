package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.api.IStorageRepository
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam

class StorageInteractorImpl(
    private val repository: IStorageRepository
): IStorageInteractor {
    override fun read(): SearchVacanciesParam {
        return repository.read()
    }

    override fun write(filterParam: SearchVacanciesParam) {
        repository.write(filterParam)
    }
}
