package com.commercial.bills.ui.screen.budget.control

import androidx.compose.runtime.Stable
import com.commercial.bills.data.entity.RecurringTransaction

@Stable
sealed class BudgetEvent {
    data class OnAddRecurringTransaction(val recurringTransaction: RecurringTransaction) : BudgetEvent()
    data class OnRecurringTransactionDelete(val recurringTransaction: RecurringTransaction) : BudgetEvent()
}