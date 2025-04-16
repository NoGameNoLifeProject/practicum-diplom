package ru.practicum.android.diploma.data.network.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.KeySkillDto
import ru.practicum.android.diploma.data.dto.SalaryDto

data class GetVacancyDetailsResponse(
    val id: String,
    val name: String,

    val area: AreaDto? = AreaDto(),

    val description: String? = null,

    val employer: EmployerDto? = null,

    @SerializedName("key_skills") val keySkills: ArrayList<KeySkillDto> = arrayListOf(),

    val salary: SalaryDto? = null,
    val salaryRange: SalaryDto? = null,

    val experience: ExperienceDto? = null,
)
