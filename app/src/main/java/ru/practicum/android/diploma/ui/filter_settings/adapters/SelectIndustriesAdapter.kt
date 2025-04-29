package ru.practicum.android.diploma.ui.filter_settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.models.Industry

class SelectIndustriesAdapter(private val onClick: (Industry) -> Unit) :
    RecyclerView.Adapter<SelectIndustriesAdapter.ViewHolder>() {

    private val industries = mutableListOf<Industry>()
    private val filteredIndustries = mutableListOf<Industry>()
    private var selectedId: String? = null

    inner class ViewHolder(val binding: IndustryItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setIndustries(newIndustries: List<Industry>) {
        industries.clear()
        industries.addAll(newIndustries)
        filteredIndustries.clear()
        filteredIndustries.addAll(newIndustries)
        notifyDataSetChanged()
    }

    fun setSelected(id: String?) {
        selectedId = id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = IndustryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            industryName.text = filteredIndustries[position].name
            industryRadioButton.isChecked = selectedId != null && selectedId == filteredIndustries[position].id

            holder.itemView.setOnClickListener {
                onClick(filteredIndustries[position])
                selectedId = filteredIndustries[position].id
            }
        }
    }

    override fun getItemCount() = filteredIndustries.size

    fun filter(expression: String) {
        val filtered = industries.filter { it.name.contains(expression, true) }
        filteredIndustries.clear()
        filteredIndustries.addAll(filtered)
        notifyDataSetChanged()
    }
}
