package com.commercial.bills.ui.screen.add_option.util

import androidx.compose.runtime.Stable
import com.commercial.bills.R

@Stable
object AddOptions {
    val options = listOf(
        AddOption(
            title = R.string.add_option_transaction_title,
            optionType = AddOptionType.TRANSACTION,
            icon = R.drawable.ic_transaction
        ),
        AddOption(
            title = R.string.add_option_recurring_transaction_title,
            optionType = AddOptionType.RECURRING_TRANSACTION,
            icon = R.drawable.ic_recurring_transaction
        ),
        AddOption(
            title = R.string.add_option_saving_plan_title,
            optionType = AddOptionType.SAVING_PLAN,
            icon = R.drawable.ic_saving_plan
        )
    )
}