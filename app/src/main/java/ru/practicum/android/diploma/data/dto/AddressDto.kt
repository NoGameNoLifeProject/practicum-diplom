package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.domain.models.Address

data class AddressDto(
    val city: String? = null,
    val street: String? = null,
    val building: String? = null
)
fun AddressDto.toDomain(): Address {
    // Собираем только непустые, ненулевые значения в список
    val parts = listOfNotNull(
        city?.takeIf { it.isNotBlank() },
        street?.takeIf { it.isNotBlank() },
        building?.takeIf { it.isNotBlank() }
    )
    // Склеиваем через “, ”; если получилось пусто — передаём null
    val fullAddress = parts
        .joinToString(separator = ", ")
        .takeIf { it.isNotEmpty() }

    return Address(address = fullAddress)
}
