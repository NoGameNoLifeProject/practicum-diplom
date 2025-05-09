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
import ru.practicum.android.diploma.databinding.FragmentSelectAreaBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.ui.filter_settings.adapters.SelectAreaAdapter
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectCountryViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectLocationViewModel

open class SelectCountryFragment : Fragment() {
    companion object {
        const val OTHER_REGIONS_ID = "1001"
    }

    private var _binding: FragmentSelectAreaBinding? = null
    val binding get() = _binding!!
    private val mainViewModel: SelectLocationViewModel by koinNavGraphViewModel<SelectLocationViewModel>(
        R.id.navigation
    )
    private val viewModel by viewModel<SelectCountryViewModel>()
    private val adapter by lazy {
        SelectAreaAdapter { country ->
            onCountryClick(country)
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
        binding.selectAreaToolbar.titleText = getString(R.string.select_country_title)
        binding.selectAreaSearchbar.isVisible = false
        binding.areaRecyclerview.adapter = adapter
        viewModel.getCountries()
        viewModel.areaState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        binding.selectAreaToolbar.setOnNavigationClick { findNavController().navigateUp() }
    }

    private fun onCountryClick(country: Area) {
        if (country.id == OTHER_REGIONS_ID) {
            val action = SelectCountryFragmentDirections.actionSelectCountryFragmentToSelectAreaFragment(country.id)
            findNavController().navigate(action)
        } else {
            mainViewModel.setCountry(country)
            findNavController().navigateUp()
        }

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

    private fun setData(state: ResourceState<List<Area>>): Boolean {
        if (state is ResourceState.Content) {
            adapter.setAreas(state.data)
            return true
        } else {
            return false
        }
    }
}
