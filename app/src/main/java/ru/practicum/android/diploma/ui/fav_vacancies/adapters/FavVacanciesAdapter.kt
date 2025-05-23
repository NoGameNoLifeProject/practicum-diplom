package ru.practicum.android.diploma.ui.fav_vacancies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.salaryFormat

class FavVacanciesAdapter(private val onClick: (VacancyDetails) -> Unit) :
    RecyclerView.Adapter<FavVacanciesAdapter.ViewHolder>() {

    private val vacancies = mutableListOf<VacancyDetails>()

    inner class ViewHolder(val binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setVacancies(newVacancies: List<VacancyDetails>) {
        vacancies.clear()
        vacancies.addAll(newVacancies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            vacancyNameCard.text = vacancies[position].name
            vacancyIndustryCard.text = vacancies[position].employer?.name
                ?: holder.itemView.context.getString(R.string.filter_parameters_industries_hint)
            vacancySalaryCard.text = salaryFormat(holder.itemView.context, vacancies[position].salary)

            Glide.with(holder.itemView)
                .load(vacancies[position].employer?.logoUrls?.original)
                .placeholder(R.drawable.ic_placeholder_32px)
                .transform(CenterInside())
                .into(vacancyCardPlaceholder)

            holder.itemView.setOnClickListener {
                onClick(vacancies[position])
            }
        }
    }

    override fun getItemCount() = vacancies.size
}
