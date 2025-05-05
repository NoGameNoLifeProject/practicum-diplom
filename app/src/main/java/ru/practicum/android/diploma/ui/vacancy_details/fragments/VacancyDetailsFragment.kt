package ru.practicum.android.diploma.ui.vacancy_details.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.domain.models.ResourceState
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.vacancy_details.view_models.VacancyDetailsViewModel
import ru.practicum.android.diploma.util.salaryFormat
import ru.practicum.android.diploma.util.setTextOrGone
import ru.practicum.android.diploma.util.workFormatSchedule

class VacancyDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = VacancyDetailsFragment()
    }

    private val args: VacancyDetailsFragmentArgs by navArgs()
    private val viewModel: VacancyDetailsViewModel by viewModel<VacancyDetailsViewModel>()
    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVacancyDetails(args.vacancyId, args.isLocal)

        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner) { isFavorite ->
            binding.toolbar.drawableAction1 =
                if (isFavorite) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_favorites_on_24px
                    )
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorites_off_24px)
                }
        }

        viewModel.vacancyDetailsState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        binding.toolbar.setOnNavigationClick { findNavController().navigateUp() }
        binding.toolbar.setOnAction1Click { viewModel.onFavoriteClicked() }
        binding.toolbar.setOnAction2Click { viewModel.shareVacancy(requireContext()) }
    }

    private fun render(state: ResourceState<VacancyDetails>) {
        binding.progressBar.isVisible = state is ResourceState.Loading
        binding.placeholder.isVisible = setPlaceholder(state)
        binding.content.isVisible = setContent(state)
    }

    private fun setPlaceholder(state: ResourceState<VacancyDetails>): Boolean {
        val didShow = if (state is ResourceState.Error){
            when (state.errorType) {
                ResourceState.ErrorType.NothingFound -> {
                    binding.placeholder.setErrorImage(R.drawable.image_error_vacancy_404)
                    binding.placeholder.setErrorText(
                        getString(R.string.vacancy_details_placeholder_not_found)
                    )
                    true
                }

                ResourceState.ErrorType.NetworkError -> {
                    binding.placeholder.setErrorImage(R.drawable.image_error_vacancy_500)
                    binding.placeholder.setErrorText(
                        getString(R.string.vacancy_details_placeholder_server_error)
                    )
                    true
                }

                ResourceState.ErrorType.NoInternet ->{
                    binding.placeholder.setErrorImage(R.drawable.image_error_no_internet)
                    binding.placeholder.setErrorText(
                        getString(R.string.no_internet)
                    )
                    true
                }

                else -> false
            }
        }else false
        return didShow
    }

    private fun setContent(state: ResourceState<VacancyDetails>): Boolean {
        when (state) {
            is ResourceState.Content -> {
                binding.vacancyName.text = state.data.name
                binding.vacancySalary.text = salaryFormat(requireContext(), state.data.salary)
                setEmployer(state.data)
                setExperience(state.data)
                binding.workFormatAndSchedule.setTextOrGone(
                    workFormatSchedule(requireContext(), state.data.workFormat, state.data.workSchedule)
                )
                setDescription(state.data)
                setKeySkills(state.data)
                return true
            }

            else -> return false
        }
    }

    private fun setEmployer(vacancy: VacancyDetails) {
        binding.employerLayout.isVisible = vacancy.employer != null
        if (vacancy.employer != null) {
            binding.employerLayout.isVisible = true
            Glide.with(requireContext())
                .load(vacancy.employer.logoUrls?.original).transform(
                    CenterInside()
                )
                .placeholder(R.drawable.ic_placeholder_32px)
                .into(binding.logo)
            binding.employerName.text = vacancy.employer.name
            binding.employerAddress.text = vacancy.address?.address ?: vacancy.area.name
        } else {
            binding.employerLayout.isVisible = false
        }
    }

    private fun setExperience(vacancy: VacancyDetails) {
        binding.experienceTitle.isVisible = vacancy.experience != null
        binding.experience.setTextOrGone(vacancy.experience?.name)

    }

    private fun setDescription(vacancy: VacancyDetails) {
        binding.description.apply {
            val html = vacancy.description
            if (html.isNullOrEmpty()) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            }
        }
        binding.descriptionTitle.isVisible = binding.description.isVisible
    }

    private fun setKeySkills(vacancy: VacancyDetails) {
        binding.keySkills.setTextOrGone(
            vacancy.keySkills
                .takeIf { it.isNotEmpty() }
                ?.joinToString(", ") { it.name }
        )
        binding.keySkillsTitle.isVisible = binding.keySkills.isVisible
    }
}
