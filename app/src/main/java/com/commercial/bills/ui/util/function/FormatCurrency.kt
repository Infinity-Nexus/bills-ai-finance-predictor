package com.commercial.bills.ui.util.function

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Float) : String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    numberFormat.maximumFractionDigits = 1

    return numberFormat.format(value)
}