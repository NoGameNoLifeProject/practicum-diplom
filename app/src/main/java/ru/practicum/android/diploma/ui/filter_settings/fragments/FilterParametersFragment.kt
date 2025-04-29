package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterParametersBinding
import ru.practicum.android.diploma.domain.models.SearchVacanciesParam
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel

class FilterParametersFragment : Fragment() {

    private val viewModel: FilterParametersViewModel by koinNavGraphViewModel<FilterParametersViewModel>(
        R.id.navigation
    )

    private var _binding: FragmentFilterParametersBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
            findNavController().navigate(R.id.selectIndustriesFragment)
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
            findNavController().navigateUp()
        }
        binding.btnReset.setOnClickListener {
            viewModel.clear()
            binding.salary.text = viewModel.salaryLiveData.value?.toString()
        }
        viewModel.filterParam.observe(viewLifecycleOwner) { param ->
            renderState(param)

        }

    }

    private fun renderState(param: SearchVacanciesParam) {
        val areaText = listOfNotNull(param.country?.name, param.areaIDs?.name).joinToString(", ")
        binding.area.setText(areaText)
        binding.industries.setText(param.industryIDs?.name)

        binding.onlyWithSalary.isChecked = param.onlyWithSalary ?: false
        val filterParamIsNotEmpty =
            listOf(param.areaIDs, param.industryIDs, param.salary).any { it != null }
                || param.onlyWithSalary == true

        binding.btnLayout.isVisible = filterParamIsNotEmpty

    }
}
