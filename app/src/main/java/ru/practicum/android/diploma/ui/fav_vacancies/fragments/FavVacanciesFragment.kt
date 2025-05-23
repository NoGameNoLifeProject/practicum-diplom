package ru.practicum.android.diploma.ui.fav_vacancies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavVacanciesBinding
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.fav_vacancies.adapters.FavVacanciesAdapter
import ru.practicum.android.diploma.ui.fav_vacancies.view_models.FavVacanciesViewModel

class FavVacanciesFragment : Fragment() {
    private var _binding: FragmentFavVacanciesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavVacanciesViewModel>()
    private var adapter: FavVacanciesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavVacanciesAdapter {
            val action =
                FavVacanciesFragmentDirections.actionFavVacanciesFragmentToVacancyDetailsFragment(it.id, true)
            findNavController().navigate(action)
        }
        binding.rvVacancies.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: ResourceState<List<VacancyDetails>>) {
        when (state) {
            is ResourceState.Loading -> showLoading()
            is ResourceState.Content -> showContent(state)
            is ResourceState.Error -> {
                when (state.errorType) {
                    ResourceState.ErrorType.Empty -> showEmpty()
                    else -> showError()
                }
            }
        }
    }

    private fun hideAll() {
        binding.rvVacancies.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorStateView.isVisible = false
    }

    private fun showLoading() {
        hideAll()
        binding.progressBar.isVisible = true
    }

    private fun showContent(state: ResourceState.Content<List<VacancyDetails>>) {
        hideAll()
        binding.rvVacancies.isVisible = true
        adapter?.setVacancies(state.data)
    }

    private fun showError() {
        hideAll()
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_error_404)
        binding.errorStateView.setErrorText(getString(R.string.fav_vacancies_placeholder_db_error))
    }

    private fun showEmpty() {
        hideAll()
        binding.errorStateView.isVisible = true
        binding.errorStateView.setErrorImage(R.drawable.image_empty_list)
        binding.errorStateView.setErrorText(getString(R.string.fav_vacancies_placeholder_empty_list))
    }

}
