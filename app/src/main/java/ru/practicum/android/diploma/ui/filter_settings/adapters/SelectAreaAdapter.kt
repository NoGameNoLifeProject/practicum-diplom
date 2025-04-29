package ru.practicum.android.diploma.ui.filter_settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaItemBinding
import ru.practicum.android.diploma.domain.models.Area

class SelectAreaAdapter(private val onClick: (Area) -> Unit) :
    RecyclerView.Adapter<SelectAreaAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AreaItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var items = mutableListOf<Area>()
    private val originalList = mutableListOf<Area>()
    private val filteredList = mutableListOf<Area>()

    fun setAreas(items: List<Area>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
        originalList.clear()
        originalList.addAll(items)
    }

    private fun updateDisplayList(updatedList: List<Area>) {
        items.clear()
        items.addAll(updatedList)
        notifyDataSetChanged()
    }

    fun filter(searchQuery: String?) {
        filteredList.clear()
        if (searchQuery.isNullOrEmpty()) {
            updateDisplayList(originalList)
        } else {
            for (item in originalList) {
                if (item.name?.contains(searchQuery.trim(), true) == true) {
                    filteredList.add(item)
                }
            }
            updateDisplayList(filteredList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AreaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            areaName.text = items[position].name
            holder.itemView.setOnClickListener {
                onClick(items[position])
            }
        }
    }

    override fun getItemCount() = items.size
}
