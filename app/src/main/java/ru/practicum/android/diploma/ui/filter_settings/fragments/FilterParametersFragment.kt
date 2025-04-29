package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterParametersBinding
import ru.practicum.android.diploma.domain.models.FilterParamsState
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel

class FilterParametersFragment : Fragment() {

    private val viewModel: FilterParametersViewModel
        by koinNavGraphViewModel<FilterParametersViewModel>(R.id.navigation)

    private var _binding: FragmentFilterParametersBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterParametersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.area.setOnClickListener {
            findNavController().navigate(R.id.action_filterParametersFragment_to_selectLocationFragment)
        }
        binding.area.setOnClearClickListener {
            viewModel.setCountry(null)
        }

        binding.industries.setOnClickListener {
            findNavController().navigate(R.id.action_filterParametersFragment_to_selectIndustriesFragment)
        }
        binding.industries.setOnClearClickListener {
            viewModel.setIndustry(null)
        }
        binding.salary.setOnTextChangedListener { text ->
            viewModel.setSalary(text.takeIf { it.isNotEmpty() }?.toInt())
        }
        binding.onlyWithSalary.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setOnlyWithSalary(isChecked)
        }

        binding.btnApply.setOnClickListener {
            viewModel.saveParam()
            setFragmentResult("updateSearch", bundleOf())
            findNavController().navigateUp()
        }
        binding.btnReset.setOnClickListener {
            viewModel.clear()
            binding.salary.text = viewModel.salaryLiveData.value?.toString()
        }
        viewModel.filterParamsState.observe(viewLifecycleOwner) { param ->
            renderState(param)
        }

        binding.toolbar.setOnNavigationClick {
            findNavController().navigateUp()
        }

    }

    private fun renderState(state: FilterParamsState?) {
        if (state == null) {
            return
        }

        val areaText = listOfNotNull(state.country?.name, state.area?.name).joinToString(", ")
        binding.area.setText(areaText)
        binding.industries.setText(state.industry?.name)

        binding.onlyWithSalary.isChecked = state.onlyWithSalary ?: false
        binding.btnApply.isVisible = state.hasDiffs
        binding.btnReset.isVisible = !state.isEmpty

    }
}
