package ru.practicum.android.diploma.domain.models

data class Area(
    val areas: ArrayList<Area> = arrayListOf(),
    val id: String? = null,
    val name: String? = null,
    val parentId: String? = null
)
