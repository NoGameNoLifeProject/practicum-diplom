package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Area

data class AreaDto(
    val areas: ArrayList<AreaDto> = arrayListOf(),
    val id: String? = null,
    val name: String? = null,
    @SerializedName("parent_id")
    val parentId: String? = null
)
fun AreaDto.toDomain(): Area = Area(
    areas = ArrayList(areas.map { it.toDomain() }),
    id = id,
    name = name,
    parentId = parentId
)
