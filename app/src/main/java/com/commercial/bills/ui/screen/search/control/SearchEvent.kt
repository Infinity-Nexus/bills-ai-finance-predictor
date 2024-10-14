package com.commercial.bills.ui.screen.search.control

import androidx.compose.runtime.Stable
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan

@Stable
sealed class SearchEvent {
    data class OnAddLastTransaction(val lastTransaction: LastTransaction) : SearchEvent()
    data class OnAddSavingPlan(val savingPlan: SavingPlan) : SearchEvent()
    data class OnAddRecurringTransaction(val recurringTransaction: RecurringTransaction) : SearchEvent()
    data class OnLastTransactionDelete(val lastTransaction: LastTransaction) : SearchEvent()
    data class OnRecurringTransactionDelete(val recurringTransaction: RecurringTransaction) : SearchEvent()
    data class OnSavingPlanDelete(val savingPlan: SavingPlan) : SearchEvent()
    data class OnSearchTextChange(val newText: String) : SearchEvent()
}