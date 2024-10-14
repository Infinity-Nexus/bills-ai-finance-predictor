package com.commercial.bills.ui.screen.home.control

import com.commercial.bills.data.entity.Balance
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.SavingPlan
import javax.annotation.concurrent.Immutable

@Immutable
data class HomeState(
    val balance: Balance? = null,
    val newBalance: String = "",
    val savingPlans: List<SavingPlan> = emptyList(),
    val lastTransactions: List<LastTransaction> = emptyList(),
    val showBalanceEdit: Boolean = false
)