package com.commercial.bills.ui.screen.add_option.util

import androidx.compose.runtime.Stable

@Stable
enum class AddOptionType {
    NONE,
    TRANSACTION,
    SAVING_PLAN,
    RECURRING_TRANSACTION
}