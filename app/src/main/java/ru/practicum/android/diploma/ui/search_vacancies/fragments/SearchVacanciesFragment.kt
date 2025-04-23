package ru.practicum.android.diploma.ui.search_vacancies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchVacanciesBinding
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.common.SearchBarView
import ru.practicum.android.diploma.ui.search_vacancies.adapter.VacancyAdapter
import ru.practicum.android.diploma.ui.search_vacancies.view_models.SearchVacanciesViewModel

class SearchVacanciesFragment : Fragment() {

    companion object {
        fun newInstance() = SearchVacanciesFragment()
    }

    private val viewModel: SearchVacanciesViewModel by viewModel<SearchVacanciesViewModel>()
    private lateinit var binding: FragmentSearchVacanciesBinding
    private val vacancies = mutableListOf<Vacancy>()
    private val searchBar = SearchBarView(requireContext())
    private var input: String? = null
    private val adapter = VacancyAdapter(vacancies) {
        findNavController().navigate(R.id.action_searchVacanciesFragment_to_vacancyDetailsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_vacancies, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolBar()
        setupRecyclerViews()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        input = searchBar.getQuery()
        searchBar.setOnQueryTextChangedListener { viewModel.searchDebounce(input!!) }
        binding.filterButton.setOnClickListener {
            binding.filterButton.setImageResource(R.drawable.ic_filter_on_24px)
            findNavController().navigate(R.id.action_searchVacanciesFragment_to_filterParametersFragment)
        }
        binding.foundedVacancy.text =
            String.format(getString(R.string.search_vacancies_chip_found_vacancies), vacancies.size.toString())
    }

    private fun setupToolBar() {
        binding.searchToolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewVacancy.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVacancy.adapter = adapter
    }

    private fun render(state: SearchVacanciesState) {
        when (state) {
            is SearchVacanciesState.VacanciesList -> {
                adapter.updateVacancy(state.vacancies)
                showVacancies()
            }

            is SearchVacanciesState.Loading -> showLoadingPage()
            is SearchVacanciesState.Empty -> showEmpty()
            is SearchVacanciesState.NetworkError -> showNetworkError()
            is SearchVacanciesState.NothingFound -> showNothingFound()
        }
    }

    private fun showVacancies() {
        binding.recyclerViewVacancy.isVisible = true
        binding.foundedVacancy.isVisible = true
        binding.placeholderLayout.isVisible = false
        binding.progressCircular.isVisible = false
    }

    private fun showLoadingPage() {
        binding.recyclerViewVacancy.isVisible = false
        binding.foundedVacancy.isVisible = false
        binding.placeholderLayout.isVisible = false
        binding.progressCircular.isVisible = true
    }

    private fun showEmpty() {
        binding.recyclerViewVacancy.isVisible = false
        binding.foundedVacancy.isVisible = false
        binding.placeholderLayout.isVisible = true
        binding.vacancyTextPlaceholder.isVisible = false
        binding.progressCircular.isVisible = false
    }

    private fun showNetworkError() {
        binding.recyclerViewVacancy.isVisible = false
        binding.foundedVacancy.isVisible = false
        binding.placeholderLayout.isVisible = true
        binding.vacancyImagePlaceholder.setImageResource(R.drawable.image_error_no_internet)
        binding.vacancyTextPlaceholder.setText(R.string.search_vacancies_no_internet)
        binding.progressCircular.isVisible = false
    }

    private fun showNothingFound() {
        binding.recyclerViewVacancy.isVisible = false
        binding.foundedVacancy.isVisible = true
        binding.placeholderLayout.isVisible = true
        binding.vacancyImagePlaceholder.setImageResource(R.drawable.image_error_404)
        binding.vacancyTextPlaceholder.setText(R.string.search_vacancies_placeholder_not_found)
        binding.progressCircular.isVisible = false
    }

}
