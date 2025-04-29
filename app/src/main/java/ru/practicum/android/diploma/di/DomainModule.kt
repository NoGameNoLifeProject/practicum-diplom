package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.CheckInternetConnectionUseCase
import ru.practicum.android.diploma.domain.api.IFavVacanciesInteractor
import ru.practicum.android.diploma.domain.api.ISharingInteractor
import ru.practicum.android.diploma.domain.api.IStorageInteractor
import ru.practicum.android.diploma.domain.api.IVacancyInteractor
import ru.practicum.android.diploma.domain.impl.FavVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.impl.StorageInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl

val domainModule = module {

    factory<IFavVacanciesInteractor> {
        FavVacanciesInteractorImpl(get())
    }

    factory<IVacancyInteractor> {
        VacancyInteractorImpl(get())
    }

    factory<ISharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<IStorageInteractor> {
        StorageInteractorImpl(get())
    }

    single {
        CheckInternetConnectionUseCase(get())
    }
}
