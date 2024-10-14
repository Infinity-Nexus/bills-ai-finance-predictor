package com.commercial.bills.data.util

import androidx.compose.runtime.Stable

@Stable
enum class RecurringType {
    ONE_TIME,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}