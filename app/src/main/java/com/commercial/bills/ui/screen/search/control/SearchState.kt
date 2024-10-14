package com.commercial.bills.ui.screen.search.control

import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import javax.annotation.concurrent.Immutable

@Immutable
data class SearchState(
    val lastTransactions: List<LastTransaction> = emptyList(),
    val recurringTransactions: List<RecurringTransaction> = emptyList(),
    val savingPlans: List<SavingPlan> = emptyList(),
    val searchText: String = ""
)