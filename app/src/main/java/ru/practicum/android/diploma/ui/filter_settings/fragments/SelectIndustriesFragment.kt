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
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.ui.filter_settings.adapters.SelectIndustriesAdapter
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectIndustriesViewModel

class SelectIndustriesFragment : Fragment() {
    private var _binding: FragmentSelectIndustriesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: FilterParametersViewModel
        by koinNavGraphViewModel<FilterParametersViewModel>(R.id.navigation)
    private val viewModel: SelectIndustriesViewModel by viewModel<SelectIndustriesViewModel>()

    private val adapter = SelectIndustriesAdapter { industry ->
        viewModel.select(industry)
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
            viewModel.select(it)
        }

        viewModel.selectedIndustry.observe(viewLifecycleOwner) {
            adapter.setSelected(it?.id)
            showSelectButton(it != null)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            if (it != null) {
                render(it)
            }
        }

        binding.selectIndustrySearchbar.setOnQueryTextChangedListener {
            viewModel.filter(it)
        }

        binding.selectIndustryToolbar.setOnNavigationClick {
            findNavController().navigateUp()
        }

        binding.selectIndustryButton.setOnClickListener {
            val selected = viewModel.selectedIndustry.value
            if (selected != null) {
                mainViewModel.setIndustry(selected)
            }
            findNavController().navigateUp()
        }
    }

    private fun render(state: ResourceState<List<Industry>>) {
        binding.progressCircular.isVisible = showLoading(state)
        binding.errorView.isVisible = showError(state)
        binding.industryRecyclerview.isVisible = showContent(state)
    }

    private fun showContent(state: ResourceState<List<Industry>>): Boolean {
        if (state is ResourceState.Content) {
            adapter.setIndustries(state.data, viewModel.selectedIndustry.value?.id)
            binding.errorView.isVisible = false
            if (binding.selectIndustrySearchbar.getQuery().isEmpty() && viewModel.selectedIndustry.value != null) {
                binding.selectIndustryButton.isVisible = true
            }
            return true
        } else {
            return false
        }
    }

    private fun showLoading(state: ResourceState<List<Industry>>): Boolean {
        if (state is ResourceState.Loading) {
            binding.selectIndustryButton.isVisible = false
            return true
        } else {
            return false
        }

    }

    private fun showFilteredEmpty() {
        binding.errorView.setErrorImage(R.drawable.image_error_404)
        binding.errorView.setErrorText(getString(R.string.select_industries_placeholder_not_found))
    }

    private fun showError(state: ResourceState<List<Industry>>): Boolean {
        if (state is ResourceState.Error) {
            binding.selectIndustryButton.isVisible = false
            when (state.errorType) {
                ResourceState.ErrorType.NoInternet -> showNoInternet()
                ResourceState.ErrorType.Empty -> showFilteredEmpty()
                else -> showNetworkError()
            }
            return true
        }
        return false
    }

    private fun showNetworkError() {
        binding.errorView.setErrorImage(R.drawable.image_error_region_500)
        binding.errorView.setErrorText(getString(R.string.select_industries_placeholder_error))
    }

    private fun showNoInternet() {
        binding.errorView.setErrorImage(R.drawable.image_error_no_internet)
        binding.errorView.setErrorText(getString(R.string.no_internet))
    }

    private fun showSelectButton(show: Boolean) {
        if (viewModel.state.value is ResourceState.Content) {
            binding.selectIndustryButton.isVisible = show
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
