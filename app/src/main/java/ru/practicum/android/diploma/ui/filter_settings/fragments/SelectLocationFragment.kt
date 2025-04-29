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
import ru.practicum.android.diploma.databinding.FragmentSelectLocationBinding
import ru.practicum.android.diploma.ui.filter_settings.view_models.SelectLocationViewModel

class SelectLocationFragment : Fragment() {

    companion object {
        fun newInstance() = SelectLocationFragment()
    }

    private var _binding: FragmentSelectLocationBinding? = null
    private val binding get() = _binding!!

    //    private val viewModel: SelectLocationViewModel by viewModel<SelectLocationViewModel>()
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
        binding.country.setOnClickListener { findNavController().navigate(
            R.id.action_selectLocationFragment_to_selectCountryFragment
        ) }
        binding.country.setOnClearClickListener {
            viewModel.setCountry(null)
            viewModel.setArea(null)
        }
        binding.area.setOnClickListener {
            val action =
                SelectLocationFragmentDirections
                    .actionSelectLocationFragmentToSelectAreaFragment(viewModel.country.value?.id)
            findNavController().navigate(action)
        }
        binding.area.setOnClearClickListener { viewModel.setArea(null) }
        renderBtn()
        viewModel.country.observe(viewLifecycleOwner) {
            binding.country.setText(it?.name)
            renderBtn()
        }
        viewModel.area.observe(viewLifecycleOwner) {
            binding.area.setText(it?.name)
            renderBtn()
        }
    }

    private fun renderBtn() {
        binding.btnApply.isVisible =
            !binding.country.getText().isNullOrEmpty() || !binding.area.getText().isNullOrEmpty()
    }
}
