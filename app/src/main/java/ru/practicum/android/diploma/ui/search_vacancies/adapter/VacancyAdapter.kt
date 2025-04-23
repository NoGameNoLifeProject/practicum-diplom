package ru.practicum.android.diploma.ui.search_vacancies.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(private var vacancyList: List<Vacancy>, private val clickListener: (Vacancy) -> Unit) :
    RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    class VacancyViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView)

}
