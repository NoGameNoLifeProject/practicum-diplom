package ru.practicum.android.diploma.ui.vacancy_details.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.vacancy_details.view_models.VacancyDetailsViewModel

class VacancyDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = VacancyDetailsFragment()
    }

    private val viewModel: VacancyDetailsViewModel by viewModel<VacancyDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_vacancy_details, container, false)
    }
}
