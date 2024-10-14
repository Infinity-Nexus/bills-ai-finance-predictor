package com.commercial.bills.ui.screen.budget.control

import com.commercial.bills.data.entity.Balance
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import javax.annotation.concurrent.Immutable

@Immutable
data class BudgetState(
    val savingPlans: List<SavingPlan> = emptyList(),
    val balance: Balance? = null,
    val recurringTransactions: List<RecurringTransaction> = emptyList()
)