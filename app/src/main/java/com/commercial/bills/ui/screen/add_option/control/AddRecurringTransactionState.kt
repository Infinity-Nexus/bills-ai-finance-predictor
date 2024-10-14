package com.commercial.bills.ui.screen.add_option.control

import com.commercial.bills.data.util.RecurringType
import javax.annotation.concurrent.Immutable

@Immutable
data class AddRecurringTransactionState(
    val addedItemName: String = "",
    val addedItemAmount: String = "",
    val addedItemIsDebit: Boolean = true,
    val addedItemRecurringType: String = RecurringType.MONTHLY.toString(),
    val isRecurringTypeDropDownExpanded: Boolean = false
)