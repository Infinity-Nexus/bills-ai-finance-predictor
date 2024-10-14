package com.commercial.bills.ui.screen.add_option.control

import com.commercial.bills.data.util.RecurringType
import java.util.Calendar
import javax.annotation.concurrent.Immutable

@Immutable
data class AddSavingPlanState(
    val showDatePicker: Boolean = false,
    val addedItemName: String = "",
    val addedItemAmount: String = "",
    val addedItemRecurringType: String = RecurringType.MONTHLY.toString(),
    val addedItemDate: Long = Calendar.getInstance().timeInMillis,
    val isRecurringTypeDropDownExpanded: Boolean = false
)