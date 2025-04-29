package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterParametersBinding
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel

class FilterParametersFragment : Fragment() {

    private val vm: FilterParametersViewModel by viewModel()

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

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { st ->
                binding.area.setText(st.location ?: "")
                binding.industries.setText(st.industry ?: "")
                binding.salaryInput.text = st.salaryFrom?.toString().orEmpty()
                binding.checkboxNoSalary.isChecked = st.hideWithoutSalary
            }
        }

        binding.area.setOnClickListener {
            findNavController().navigate(R.id.action_filterParametersFragment_to_selectLocationFragment)
        }
        binding.industries.setOnClickListener {
            findNavController().navigate(R.id.action_filterParametersFragment_to_selectIndustriesFragment)
        }

        binding.toolbar.setOnNavigationClick {
            findNavController().popBackStack()
        }

        binding.btnReset.setOnClickListener {
            vm.clearFilters()
            //binding.salaryInput.setText("")
        }

        binding.btnApply.setOnClickListener {
            vm.saveFilters()
            //vm.updateFilterIcon()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
