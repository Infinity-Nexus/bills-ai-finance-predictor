package com.commercial.bills.ui.screen.add_option.control

import java.util.Calendar
import javax.annotation.concurrent.Immutable

@Immutable
data class AddTransactionState(
    val showDatePicker: Boolean = false,
    val addedItemName: String = "",
    val addedItemAmount: String = "",
    val addedItemIsDebit: Boolean = true,
    val addedItemDate: Long = Calendar.getInstance().timeInMillis
)