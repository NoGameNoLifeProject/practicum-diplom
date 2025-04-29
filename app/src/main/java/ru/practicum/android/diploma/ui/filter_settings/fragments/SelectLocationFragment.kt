package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectLocationBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectLocationViewModel

class SelectLocationFragment : Fragment() {

    companion object {
        fun newInstance() = SelectLocationFragment()
    }

    private var _binding: FragmentSelectLocationBinding? = null
    private val binding get() = _binding!!

    private val filterParamViewModel: FilterParametersViewModel by koinNavGraphViewModel<FilterParametersViewModel>(
        R.id.navigation
    )
    private val viewModel: SelectLocationViewModel by koinNavGraphViewModel<SelectLocationViewModel>(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.country.value == null && viewModel.area.value == null) {
            viewModel.setCountry(filterParamViewModel.countryLiveData.value)
            viewModel.setArea(filterParamViewModel.areaLiveData.value)
        }

        binding.country.setOnClickListener {
            findNavController().navigate(
                R.id.action_selectLocationFragment_to_selectCountryFragment
            )
        }
        binding.country.setOnClearClickListener {
            viewModel.setCountry(null)
        }

        binding.area.setOnClickListener {
            val action =
                SelectLocationFragmentDirections
                    .actionSelectLocationFragmentToSelectAreaFragment(viewModel.country.value?.id)
            findNavController().navigate(action)
        }
        binding.area.setOnClearClickListener { viewModel.setArea(null) }

        viewModel.country.observe(viewLifecycleOwner) {
            binding.country.setText(it?.name)
            renderBtn(it)
        }
        viewModel.area.observe(viewLifecycleOwner) {
            binding.area.setText(it?.name)
        }
        binding.toolbar.setOnNavigationClick { clearAndGoUp() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { clearAndGoUp() }

        binding.btnApply.setOnClickListener { saveAndGoUp() }
    }

    private fun clearAndGoUp() {
        viewModel.setArea(null)
        viewModel.setCountry(null)
        findNavController().navigateUp()
    }

    private fun saveAndGoUp() {
        filterParamViewModel.setCountry(viewModel.country.value)
        filterParamViewModel.setArea(viewModel.area.value)
        viewModel.setArea(null)
        viewModel.setCountry(null)
        findNavController().navigateUp()
    }

    private fun renderBtn(country: Area?) {
        binding.btnApply.isVisible = country != null
    }
}
