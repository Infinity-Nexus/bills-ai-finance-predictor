package com.commercial.bills.ui.util.function

import java.util.Calendar
import java.util.Locale

fun formatDate(date: Calendar) : String {
    val dayOfMonth = date
        .get(Calendar.DAY_OF_MONTH)
        .toString()
    val month = date.getDisplayName(
        Calendar.MONTH,
        Calendar.SHORT,
        Locale.getDefault()
    )
    val year = date
        .get(Calendar.YEAR)
        .toString()

    return "$month $dayOfMonth, $year"
}