package com.commercial.bills.ui.screen.add_option.control

import androidx.compose.runtime.Stable
import com.commercial.bills.data.entity.RecurringTransaction

@Stable
sealed class AddRecurringTransactionEvent {
    data object OnRecurringTransactionDialogClose : AddRecurringTransactionEvent()
    data class OnAddedItemNameChange(val newName: String) : AddRecurringTransactionEvent()
    data class OnAddedItemAmountChange(val newAmount: String) : AddRecurringTransactionEvent()
    data class OnAddedItemIsDebitChange(val isDebit: Boolean) : AddRecurringTransactionEvent()
    data class OnAddedItemRecurringType(val newRecurringType: String) : AddRecurringTransactionEvent()
    data class OnAddRecurringTransaction(val recurringTransaction: RecurringTransaction) : AddRecurringTransactionEvent()
    data class OnIsRecurringTypeDropDownExpandedChange(val isDropDownExpanded: Boolean) : AddRecurringTransactionEvent()
}