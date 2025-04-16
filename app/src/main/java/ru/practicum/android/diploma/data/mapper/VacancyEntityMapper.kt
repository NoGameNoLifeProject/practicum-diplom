package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.db.FavVacancyEntity
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.KeySkillDto
import ru.practicum.android.diploma.data.dto.LogoUrlsDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto

class VacancyEntityMapper {
    private fun convertToVacancy(entity: FavVacancyEntity): VacancyDto {
        val employer = EmployerDto(
            id = entity.employerId,
            name = entity.employerName,
            logoUrls = LogoUrlsDto(original = entity.employerLogoUrl)
        )
        val salary = SalaryDto(
            currency = entity.salaryCurrency.ifEmpty { null },
            from = if (entity.salaryFrom == 0) null else entity.salaryFrom,
            gross = entity.salaryGross,
            to = entity.salaryTo.ifEmpty { null }
        )
        return VacancyDto(
            id = entity.id,
            name = entity.name,
            employer = employer,
            salary = salary,
            salaryRange = null
        )
    }

    fun convertToVacancyDetails(entity: FavVacancyEntity): VacancyDetailsDto {
        val vacancy = convertToVacancy(entity)
        val area = AreaDto(id = entity.areaId, name = entity.areaName)
        val experience = ExperienceDto(id = entity.experienceId, name = entity.experienceName)
        val keySkills = entity.keySkills.split(",").map { KeySkillDto(it) }

        return VacancyDetailsDto(
            id = vacancy.id,
            name = vacancy.name,
            area = area,
            description = entity.description,
            employer = vacancy.employer,
            keySkills = ArrayList(keySkills),
            salary = vacancy.salary,
            salaryRange = vacancy.salaryRange,
            experience = experience
        )
    }

    @Suppress("CyclomaticComplexMethod")
    fun convertFromVacancyDetails(vacancy: VacancyDetailsDto): FavVacancyEntity {
        val salary = if (vacancy.salary != null) {
            SalaryDto(
                currency = vacancy.salary.currency ?: "",
                from = vacancy.salary.from ?: 0,
                gross = vacancy.salary.gross ?: false,
                to = vacancy.salary.to ?: ""
            )
        } else {
            SalaryDto(
                currency = vacancy.salaryRange?.currency ?: "",
                from = vacancy.salaryRange?.from ?: 0,
                gross = vacancy.salaryRange?.gross ?: false,
                to = vacancy.salaryRange?.to ?: ""
            )
        }

        return FavVacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            areaId = vacancy.area?.id ?: "",
            areaName = vacancy.area?.name ?: "",
            description = vacancy.description ?: "",
            employerId = vacancy.employer?.id ?: "",
            employerName = vacancy.employer?.name ?: "",
            employerLogoUrl = vacancy.employer?.logoUrls?.original ?: "",
            keySkills = vacancy.keySkills.joinToString(","),
            salaryFrom = salary.from ?: 0,
            salaryGross = salary.gross ?: false,
            salaryTo = salary.to ?: "",
            salaryCurrency = salary.currency ?: "",
            experienceId = vacancy.experience?.id ?: "",
            experienceName = vacancy.experience?.name ?: ""
        )
    }
}
