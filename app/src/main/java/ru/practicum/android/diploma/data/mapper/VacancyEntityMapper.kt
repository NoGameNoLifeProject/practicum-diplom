package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.db.FavVacancyEntity
import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.KeySkill
import ru.practicum.android.diploma.domain.models.LogoUrls
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.models.WorkFormat
import ru.practicum.android.diploma.domain.models.WorkSchedule

class VacancyEntityMapper {
    private fun convertToVacancy(entity: FavVacancyEntity): Vacancy {
        val employer = Employer(
            id = entity.employerId,
            name = entity.employerName,
            logoUrls = LogoUrls(original = entity.employerLogoUrl)
        )
        val salary = Salary(
            currency = entity.salaryCurrency.ifEmpty { null },
            from = if (entity.salaryFrom == 0) null else entity.salaryFrom,
            gross = entity.salaryGross,
            to = if (entity.salaryTo == 0) null else entity.salaryTo
        )
        return Vacancy(
            id = entity.id,
            name = entity.name,
            employer = employer,
            salary = salary,
            salaryRange = null
        )
    }

    fun convertToVacancyDetails(entity: FavVacancyEntity): VacancyDetails {
        val vacancy = convertToVacancy(entity)
        val area = Area(id = entity.areaId, name = entity.areaName)
        val experience = if (entity.experienceId.isNotEmpty() && entity.experienceName.isNotEmpty()) {
            Experience(id = entity.experienceId, name = entity.experienceName)
        } else {
            null
        }
        val keySkills = entity.keySkills.split(",").filter { it.isNotEmpty() }.map { KeySkill(it) }
        val workFormat = if (entity.workFormat.isNotEmpty()) {
            entity.workFormat.split(",").mapIndexed { i, name -> WorkFormat(id = i.toString(), name) }
        } else {
            null
        }
        val workSchedule = if (entity.workSchedule.isNotEmpty()) {
            entity.workSchedule.split(",").mapIndexed { i, name -> WorkSchedule(id = i.toString(), name) }
        } else {
            null
        }

        return VacancyDetails(
            id = vacancy.id,
            name = vacancy.name,
            area = area,
            description = entity.description,
            employer = vacancy.employer,
            keySkills = ArrayList(keySkills),
            salary = vacancy.salary,
            salaryRange = vacancy.salaryRange,
            experience = experience,
            alternateUrl = entity.alternateUrl,
            workFormat = workFormat,
            workSchedule = workSchedule,
            address = Address(entity.address)
        )
    }

    @Suppress("CyclomaticComplexMethod")
    fun convertFromVacancyDetails(vacancy: VacancyDetails): FavVacancyEntity {
        val salary = if (vacancy.salary != null) {
            Salary(
                currency = vacancy.salary.currency ?: "",
                from = vacancy.salary.from ?: 0,
                gross = vacancy.salary.gross ?: false,
                to = vacancy.salary.to ?: 0
            )
        } else {
            Salary(
                currency = vacancy.salaryRange?.currency ?: "",
                from = vacancy.salaryRange?.from ?: 0,
                gross = vacancy.salaryRange?.gross ?: false,
                to = vacancy.salaryRange?.to ?: 0
            )

        }

        return FavVacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            areaId = vacancy.area.id ?: "",
            areaName = vacancy.area.name ?: "",
            description = vacancy.description ?: "",
            employerId = vacancy.employer?.id ?: "",
            employerName = vacancy.employer?.name ?: "",
            employerLogoUrl = vacancy.employer?.logoUrls?.original ?: "",
            keySkills = vacancy.keySkills.map { it.name }.joinToString(","),
            salaryFrom = salary.from ?: 0,
            salaryGross = salary.gross ?: false,
            salaryTo = salary.to ?: 0,
            salaryCurrency = salary.currency ?: "",
            experienceId = vacancy.experience?.id ?: "",
            experienceName = vacancy.experience?.name ?: "",
            alternateUrl = vacancy.alternateUrl,
            workFormat = vacancy.workFormat?.joinToString(",") { it.name } ?: "",
            workSchedule = vacancy.workSchedule?.joinToString(",") { it.name } ?: "",
            address = vacancy.address?.address ?: ""
        )
    }
}
