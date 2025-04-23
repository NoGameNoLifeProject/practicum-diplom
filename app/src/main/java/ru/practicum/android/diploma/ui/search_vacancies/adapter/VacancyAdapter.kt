package ru.practicum.android.diploma.ui.search_vacancies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    private var vacancyList: List<Vacancy>,
    private val clickListener: (Vacancy) -> Unit
) :
    RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    class VacancyViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
        private val vacancyName: TextView = parentView.findViewById(R.id.vacancy_name_card)
        private val industryName: TextView = parentView.findViewById(R.id.vacancy_industry_card)
        private val salary: TextView = parentView.findViewById(R.id.vacancy_salary_card)
        private val vacancyImage: ImageView = parentView.findViewById(R.id.vacancy_card_placeholder)

        fun bind(vacancy: Vacancy) {
            vacancyName.text = vacancy.name
            industryName.text = vacancy.employer?.name
            salary.text = vacancy.salary?.currency
            Glide.with(vacancyImage)
                .load(vacancy.employer?.logoUrls?.size90)
                .placeholder(R.drawable.ic_placeholder_32px)
                .transform(RoundedCorners(10))
                .submit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return VacancyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancyList[position])
        holder.itemView.setOnClickListener {
            clickListener.invoke(vacancyList[position])
        }
    }

    fun updateVacancy(newVacancy: List<Vacancy>) {
        vacancyList = newVacancy
        notifyDataSetChanged()
    }
}
