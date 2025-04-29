package ru.practicum.android.diploma.ui.search_vacancies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchVacanciesBinding
import ru.practicum.android.diploma.domain.models.SearchVacanciesState
import ru.practicum.android.diploma.ui.search_vacancies.adapter.VacancyAdapter
import ru.practicum.android.diploma.ui.search_vacancies.view_models.SearchVacanciesViewModel
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.domain.api.IStorageRepository


class SearchVacanciesFragment : Fragment() {
    private var _binding: FragmentSearchVacanciesBinding? = null
    private val binding get() = _binding!!
    private val storage: IStorageRepository by inject()

    val viewModel: SearchVacanciesViewModel by viewModel { parametersOf(storage) }

    private val adapter = VacancyAdapter {
        val action =
            SearchVacanciesFragmentDirections.actionSearchVacanciesFragmentToVacancyDetailsFragment(it.id, false)
        findNavController().navigate(action)
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

        binding.searchToolBar.setOnAction1Click {
            findNavController().navigate(R.id.action_searchVacanciesFragment_to_filterParametersFragment)
        }

        binding.recyclerViewVacancy.adapter = adapter
        configurePagination()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.searchBar.setOnQueryTextChangedListener {
            viewModel.searchVacancies(it)
        }

        binding.searchBar.setOnSearchIconClickListener {
            viewModel.clearSearchExpression()
        }

        binding.searchToolBar.setOnAction1Click {
            findNavController().navigate(R.id.action_searchVacanciesFragment_to_filterParametersFragment)
        }

        viewModel.showToast.observe(viewLifecycleOwner) {
            adapter.isLoadingMore = false
            showToast(it)
        }

        viewModel.filterIcon.observe(viewLifecycleOwner) {
            updateFilterIcon(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFilterIcon()
    }

    private fun render(state: SearchVacanciesState) {
        when (state) {
            is SearchVacanciesState.Content -> showVacancies(state)
            is SearchVacanciesState.Loading -> showLoadingPage()
            is SearchVacanciesState.Error -> showError(state)
        }
    }

    private fun hideAll() {
        binding.errorStateView.isVisible = false
        binding.recyclerViewVacancy.isVisible = false
        binding.progressCircular.isVisible = false
        binding.foundedVacancy.isVisible = false
    }

    private fun showVacancies(state: SearchVacanciesState.Content) {
        hideAll()
        binding.recyclerViewVacancy.isVisible = true
        binding.foundedVacancy.isVisible = true
        binding.foundedVacancy.text =
            String.format(getString(R.string.search_vacancies_chip_found_vacancies), state.founded.toString())
        adapter.setVacancies(state.items)
        adapter.isLoadingMore = state.isLoadingMore
    }

    private fun showLoadingPage() {
        hideAll()
        binding.progressCircular.isVisible = true
    }

    private fun showError(state: SearchVacanciesState.Error) {
        hideAll()
        when (state.errorType) {
            SearchVacanciesState.ErrorType.Empty -> showEmpty()
            SearchVacanciesState.ErrorType.NetworkError -> showNetworkError()
            SearchVacanciesState.ErrorType.NothingFound -> showNothingFound()
            SearchVacanciesState.ErrorType.NoInternet -> showNoInternet()
        }
    }

    private fun showEmpty() {
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_search)
        binding.errorStateView.setErrorText("")
    }

    private fun showNetworkError() {
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_500)
        binding.errorStateView.setErrorText(getString(R.string.search_vacancies_placeholder_server_error))
    }

    private fun showNothingFound() {
        binding.foundedVacancy.isVisible = true
        binding.foundedVacancy.text =
            String.format(getString(R.string.search_vacancies_chip_not_found))

        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_404)
        binding.errorStateView.setErrorText(getString(R.string.search_vacancies_placeholder_not_found))
    }

    private fun showNoInternet() {
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_no_internet)
        binding.errorStateView.setErrorText(getString(R.string.search_vacancies_no_internet))
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(context, getString(resId), Toast.LENGTH_SHORT).show()
    }

    private fun configurePagination() {
        binding.recyclerViewVacancy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.recyclerViewVacancy.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = binding.recyclerViewVacancy.adapter?.itemCount ?: 0
                    if (pos >= itemsCount - 1) {
                        binding.recyclerViewVacancy.post {
                            viewModel.loadNewVacanciesPage()
                        }
                    }
                }
            }
        })
    }

    private fun updateFilterIcon(iconRes: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), iconRes)!!
        binding.searchToolBar.drawableAction1 = drawable
    }
}
