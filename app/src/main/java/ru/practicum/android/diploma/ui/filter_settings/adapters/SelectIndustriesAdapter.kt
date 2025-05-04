package ru.practicum.android.diploma.ui.filter_settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.models.Industry

class SelectIndustriesAdapter(
    private val onClick: (Industry) -> Unit
) :
    RecyclerView.Adapter<SelectIndustriesAdapter.ViewHolder>() {

    private val filteredIndustries = mutableListOf<Industry>()
    private var selectedId: String? = null

    inner class ViewHolder(val binding: IndustryItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setIndustries(newIndustries: List<Industry>, selected: String? = null) {
        setSelected(selected)
        filteredIndustries.clear()
        filteredIndustries.addAll(newIndustries)
        notifyDataSetChanged()
    }

    fun setSelected(id: String?) {
        selectedId = id
        notifyDataSetChanged()
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
            }
        }
    }

    override fun getItemCount() = filteredIndustries.size
}
