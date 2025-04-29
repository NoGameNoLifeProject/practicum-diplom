package ru.practicum.android.diploma.domain.models

data class Industry(
    val id: String,
    val name: String,
    val subIndustries: List<Industry>?
)

fun Industry.flatten(): List<Industry> =
    listOf(this) + (subIndustries?.flatMap { it.flatten() } ?: emptyList())
