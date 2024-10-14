package com.commercial.bills.ui.screen.add_option.control

import androidx.compose.runtime.Stable
import com.commercial.bills.data.entity.LastTransaction

@Stable
sealed class AddTransactionEvent {
    data class OnShowDatePickerChange(val showDatePicker: Boolean) : AddTransactionEvent()
    data class OnAddedItemDate(val newDate: Long) : AddTransactionEvent()
    data object OnTransactionDialogClose : AddTransactionEvent()
    data class OnAddedItemNameChange(val newName: String) : AddTransactionEvent()
    data class OnAddedItemAmountChange(val newAmount: String) : AddTransactionEvent()
    data class OnAddedItemIsDebitChange(val isDebit: Boolean) : AddTransactionEvent()
    data class OnAddTransaction(val transaction: LastTransaction) : AddTransactionEvent()
}