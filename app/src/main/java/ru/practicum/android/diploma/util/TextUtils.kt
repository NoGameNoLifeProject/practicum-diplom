package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Salary
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
