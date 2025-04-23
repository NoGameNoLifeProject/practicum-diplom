package ru.practicum.android.diploma.util

import android.content.Context
import android.view.View
import android.widget.TextView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.WorkFormat
import ru.practicum.android.diploma.domain.models.WorkSchedule
import java.util.Currency

fun salaryFormat(context: Context, salary: Salary?): String {
    var result = ""
    if (salary == null) {
        result = context.getString(
            R.string.vacancy_salary_not_specified
        )
    } else if (salary.from != null && salary.to != null) {
        result = context.getString(
            R.string.vacancy_salary_from_to,
            salary.from.toString(),
            salary.to.toString(),
            getCurrency(salary.currency)
        )
    } else if (salary.from != null) {
        result = context.getString(
            R.string.vacancy_salary_from,
            salary.from.toString(),
            getCurrency(salary.currency)
        )
    } else if (salary.to != null) {
        result = context.getString(
            R.string.vacancy_salary_to,
            salary.to.toString(),
            getCurrency(salary.currency)
        )
    }

    return result
}

fun getCurrency(currency: String?): String {
    if (currency == null) {
        return ""
    }

    return if (currency == "RUR") {
        "₽"
    } else {
        Currency.getInstance(currency).symbol
    }
}

fun workFormatSchedule(
    context: Context,
    listWorkFormat: List<WorkFormat>?,
    lisWorkSchedule: List<WorkSchedule>?
): String {
    var result = ""
    var workFormat = ""
    var workSchedule = ""
    if (!listWorkFormat.isNullOrEmpty() && !lisWorkSchedule.isNullOrEmpty()) {
        workFormat = listWorkFormat.joinToString(",") { it.name }
        workSchedule = lisWorkSchedule.joinToString(",") { it.name }
        result = context.getString(R.string.vacancy_details_work_format_work_schedule, workFormat, workSchedule)
    } else if (!listWorkFormat.isNullOrEmpty()) {
        workFormat = listWorkFormat.joinToString(",") { it.name }
        result = workFormat
    } else if (!lisWorkSchedule.isNullOrEmpty()) {
        workSchedule = lisWorkSchedule.joinToString(",") { it.name }
        result = workSchedule
    }
    return result
}

/** Устанавливает текст или прячет TextView, если текст null или пустой */
fun TextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        this.text = text
    }
}

/** Устанавливает formatted-текст или прячет TextView, если форматер вернул null/empty */
fun TextView.setFormattedTextOrGone(formatted: CharSequence?) {
    if (formatted.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        text = formatted
    }
}
