package ru.practicum.android.diploma.ui.search_vacancies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.databinding.VacancyItemLoadingBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.salaryFormat

class VacancyAdapter(private val onClick: (Vacancy) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val vacancies = mutableListOf<Vacancy>()
    var isLoadingMore = false

    inner class ViewHolderVacancy(val binding: VacancyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ViewHolderLoading(val binding: VacancyItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position < vacancies.size) R.layout.vacancy_item else R.layout.vacancy_item_loading
    }

    fun setVacancies(newVacancies: List<Vacancy>) {
        vacancies.clear()
        vacancies.addAll(newVacancies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.vacancy_item -> {
                val binding =
                    VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolderVacancy(binding)
            }

            else -> {
                val binding = VacancyItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderLoading(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.vacancy_item -> {
                with((holder as ViewHolderVacancy).binding) {
                    vacancyNameCard.text = vacancies[position].name
                    vacancyIndustryCard.text = vacancies[position].employer?.name
                        ?: holder.itemView.context.getString(R.string.filter_parameters_industries_hint)
                    vacancySalaryCard.text =
                        salaryFormat(holder.itemView.context, vacancies[position].salary)

                    Glide.with(holder.itemView)
                        .load(vacancies[position].employer?.logoUrls?.size240)
                        .placeholder(R.drawable.ic_placeholder_32px)
                        .transform(
                            CenterInside()
                        )
                        .into(vacancyCardPlaceholder)

                    holder.itemView.setOnClickListener {
                        onClick(vacancies[position])
                    }
                }
            }

            R.layout.vacancy_item_loading -> {
                with((holder as ViewHolderLoading).binding) {}
            }
        }

    }

    override fun getItemCount() = vacancies.size + if (isLoadingMore) 1 else 0
}
