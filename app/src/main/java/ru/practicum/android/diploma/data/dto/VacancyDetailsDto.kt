package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailsDto(
    val id: String,
    val name: String,
    val area: AreaDto = AreaDto(),
    val description: String? = null,
    val employer: EmployerDto? = null,

    @SerializedName("key_skills") val keySkills: List<KeySkillDto> = arrayListOf(),

    val salary: SalaryDto? = null,
    val salaryRange: SalaryDto? = null,
    val experience: ExperienceDto? = null,

    @SerializedName("alternate_url") val alternateUrl: String,
    @SerializedName("work_format") val workFormat: List<WorkFormatDto>? = null,
    @SerializedName("work_schedule_by_days") val workSchedule: List<WorkScheduleDto>? = null,
    val address: AddressDto? = null
)
