package ru.practicum.android.diploma.ui.search_vacancies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.search_vacancies.view_models.SearchVacanciesViewModel

class SearchVacanciesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchVacanciesFragment()
    }

    private val viewModel: SearchVacanciesViewModel by viewModel<SearchVacanciesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_vacancies, container, false)
    }
}
