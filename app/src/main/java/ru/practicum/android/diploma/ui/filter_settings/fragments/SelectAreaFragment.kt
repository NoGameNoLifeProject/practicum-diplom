package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectAreaBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.ui.filter_settings.adapters.SelectAreaAdapter
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectAreaViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectLocationViewModel

class SelectAreaFragment : Fragment() {

    private var _binding: FragmentSelectAreaBinding? = null
    val binding get() = _binding!!
    private val args: SelectAreaFragmentArgs by navArgs()
    private val locationViewModel: SelectLocationViewModel by koinNavGraphViewModel<SelectLocationViewModel>(
        R.id.navigation
    )
    private val viewModel by viewModel<SelectAreaViewModel>()
    private val adapter by lazy {
        SelectAreaAdapter { area ->
            onAreaClick(area)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.areaRecyclerview.adapter = adapter
        viewModel.getAreas(args.countryId)
        viewModel.areaState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        binding.selectAreaSearchbar.setOnQueryTextChangedListener {
            adapter.filter(it)
            if (viewModel.areaState.value is ResourceState.Content) {
                setEmptyPlaceholder(adapter.itemCount)
            }
        }
        binding.selectAreaToolbar.setOnNavigationClick { findNavController().navigateUp() }
    }

    private fun onAreaClick(area: Area) {
        val country = viewModel.getCountryByArea(area)
        locationViewModel.setCountry(country)
        locationViewModel.setArea(area)
        findNavController().popBackStack(R.id.selectLocationFragment, false)
    }

    private fun render(state: ResourceState<List<Area>>) {
        binding.progressBar.isVisible = state is ResourceState.Loading
        binding.placeholder.isVisible = setPlaceholder(state)
        binding.areaRecyclerview.isVisible = setData(state)
    }

    private fun setPlaceholder(state: ResourceState<List<Area>>): Boolean {
        if (state is ResourceState.Error) {
            when (state.errorType) {
                ResourceState.ErrorType.NoInternet -> showNoInternet()
                else -> showNetworkError()
            }
            binding.placeholder.isVisible = true
            return true
        } else {
            return false
        }
    }

    private fun showNetworkError() {
        binding.placeholder.setErrorText(getString(R.string.select_area_placeholder_error))
        binding.placeholder.setErrorImage(R.drawable.image_error_region_500)
    }

    private fun showNoInternet() {
        binding.placeholder.setErrorImage(R.drawable.image_error_no_internet)
        binding.placeholder.setErrorText(getString(R.string.no_internet))
    }

    private fun setEmptyPlaceholder(size: Int) {
        if (size == 0) {
            binding.areaRecyclerview.isVisible = false
            binding.placeholder.isVisible = true
            binding.placeholder.setErrorText(getString(R.string.select_area_placeholder_not_found))
            binding.placeholder.setErrorImage(R.drawable.image_error_404)
        } else {
            binding.placeholder.isVisible = false
            binding.areaRecyclerview.isVisible = true
        }
    }

    private fun setData(state: ResourceState<List<Area>>): Boolean {
        if (state is ResourceState.Content) {
            adapter.setAreas(state.data)
            return true
        } else {
            return false
        }
    }

}
