package ru.practicum.android.diploma.ui.search_vacancies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchVacanciesBinding
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.ui.search_vacancies.adapter.VacancyAdapter
import ru.practicum.android.diploma.ui.search_vacancies.view_models.SearchVacanciesViewModel

class SearchVacanciesFragment : Fragment() {
    private var _binding: FragmentSearchVacanciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchVacanciesViewModel by viewModel<SearchVacanciesViewModel>()

    private val adapter = VacancyAdapter {
        findNavController().navigate(R.id.action_searchVacanciesFragment_to_vacancyDetailsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewVacancy.adapter = adapter
		configurePagination()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.searchBar.setOnQueryTextChangedListener {
            viewModel.searchVacancies(it)
        }

        binding.searchToolBar.setOnAction1Click {
            findNavController().navigate(R.id.action_searchVacanciesFragment_to_filterParametersFragment)
        }
    }

    private fun render(state: SearchVacanciesState) {
        when (state) {
            is SearchVacanciesState.VacanciesList -> showVacancies(state)
            is SearchVacanciesState.Loading -> showLoadingPage()
            is SearchVacanciesState.Empty -> showEmpty()
            is SearchVacanciesState.NetworkError -> showNetworkError()
            is SearchVacanciesState.NoInternet -> showNoInternet()
            is SearchVacanciesState.NothingFound -> showNothingFound()
        }
    }

    private fun hideAll() {
        binding.errorStateView.isVisible = false
        binding.recyclerViewVacancy.isVisible = false
        binding.progressCircular.isVisible = false
        binding.foundedVacancy.isVisible = false

    }

    private fun showVacancies(state: SearchVacanciesState.VacanciesList) {
        hideAll()
        binding.recyclerViewVacancy.isVisible = true
        binding.foundedVacancy.isVisible = true
        binding.foundedVacancy.text =
            String.format(getString(R.string.search_vacancies_chip_found_vacancies), state.vacancies.size.toString())
        adapter.setVacancies(state.vacancies)
    }

    private fun showLoadingPage() {
        hideAll()
        binding.progressCircular.isVisible = true
    }

    private fun showEmpty() {
        hideAll()
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_search)
        binding.errorStateView.setErrorText("")
    }

    private fun showNetworkError() {
        hideAll()
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_500)
        binding.errorStateView.setErrorText(getString(R.string.search_vacancies_placeholder_server_error))
    }

    private fun showNothingFound() {
        hideAll()
        binding.foundedVacancy.isVisible = true
        binding.foundedVacancy.text =
            String.format(getString(R.string.search_vacancies_chip_not_found))

        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_404)
        binding.errorStateView.setErrorText(getString(R.string.search_vacancies_placeholder_not_found))
    }

    private fun showNoInternet() {
        hideAll()
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_no_internet)
        binding.errorStateView.setErrorText(getString(R.string.search_vacancies_no_internet))
    }


    private fun configurePagination() {
        binding.recyclerViewVacancy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.recyclerViewVacancy.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = binding.recyclerViewVacancy.adapter?.itemCount ?: 0
                    if (pos >= itemsCount - 1) {
                        viewModel.loadNewVacanciesPage()
                    }
                }
            }
        })
    }
}
