package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectIndustriesBinding
import ru.practicum.android.diploma.domain.models.SelectIndustriesScreenState
import ru.practicum.android.diploma.ui.filter_settings.adapters.SelectIndustriesAdapter
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectIndustriesViewModel

class SelectIndustriesFragment : Fragment() {
    private var _binding: FragmentSelectIndustriesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: FilterParametersViewModel
        by koinNavGraphViewModel<FilterParametersViewModel>(R.id.navigation)
    private val viewModel: SelectIndustriesViewModel by viewModel<SelectIndustriesViewModel>()

    private val adapter = SelectIndustriesAdapter {
        mainViewModel.setIndustry(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectIndustriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.industryRecyclerview.adapter = adapter

        mainViewModel.industryLiveData.observe(viewLifecycleOwner) {
            adapter.setSelected(it?.id)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.selectIndustrySearchbar.setOnQueryTextChangedListener {
            adapter.filter(it)
        }

        binding.selectIndustryToolbar.setOnNavigationClick {
            findNavController().navigateUp()
        }
    }

    private fun render(state: SelectIndustriesScreenState) {
        when (state) {
            is SelectIndustriesScreenState.Loading -> showLoading()
            is SelectIndustriesScreenState.Content -> showContent(state)
            is SelectIndustriesScreenState.Error -> showError()
        }
    }

    private fun hideAll() {
        binding.industryRecyclerview.isVisible = false
        binding.progressCircular.isVisible = false
        binding.errorView.isVisible = false
    }

    private fun showLoading() {
        hideAll()
        binding.progressCircular.isVisible = true
    }

    private fun showContent(state: SelectIndustriesScreenState.Content) {
        hideAll()
        binding.industryRecyclerview.isVisible = true
        adapter.setIndustries(state.industries)
    }

    private fun showError() {
        hideAll()
        binding.errorView.isVisible = true
        binding.errorView.setErrorImage(R.drawable.image_error_region_500)
        binding.errorView.setErrorText(getString(R.string.select_industries_placeholder_error))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
