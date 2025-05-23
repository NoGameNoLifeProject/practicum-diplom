package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.fav_vacancies.view_models.FavVacanciesViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectAreaViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectCountryViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectIndustriesViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectLocationViewModel
import ru.practicum.android.diploma.ui.search_vacancies.view_models.SearchVacanciesViewModel
import ru.practicum.android.diploma.ui.vacancy_details.view_models.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        FavVacanciesViewModel(get())
    }

    viewModel {
        FilterParametersViewModel(get())
    }

    viewModel {
        SelectAreaViewModel(get())
    }
    viewModel {
        SelectCountryViewModel(get())
    }

    viewModel {
        SelectIndustriesViewModel(vacancyInteractor = get())
    }

    viewModel {
        SelectLocationViewModel()
    }

    viewModel {
        SearchVacanciesViewModel(get(), get())
    }

    viewModel {
        VacancyDetailsViewModel(vacancyInteractor = get(), favoriteInteractor = get())
    }

}
