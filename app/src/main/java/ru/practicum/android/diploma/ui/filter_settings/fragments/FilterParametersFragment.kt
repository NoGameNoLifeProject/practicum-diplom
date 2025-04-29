package ru.practicum.android.diploma.ui.filter_settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterParametersBinding
import ru.practicum.android.diploma.ui.filter_settings.view_models.FilterParametersViewModel

class FilterParametersFragment : Fragment() {

    private val viewModel: FilterParametersViewModel by viewModel<FilterParametersViewModel>()

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
        binding.industries.setOnClickListener {
            binding.industries.setText("Россия")
        }
    }
}
