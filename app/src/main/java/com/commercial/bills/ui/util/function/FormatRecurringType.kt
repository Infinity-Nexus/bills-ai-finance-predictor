package com.commercial.bills.ui.util.function

import com.commercial.bills.data.util.RecurringType

fun formatRecurringType(recurringType: RecurringType): String {
    return recurringType
        .toString()
        .lowercase()
        .split('_')
        .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
}