package com.commercial.bills.ui.screen.home.control

import androidx.compose.runtime.Stable
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.SavingPlan

@Stable
sealed class HomeEvent {
    data class OnBalanceChange(val newBalance: Float) : HomeEvent()
    data class OnShowEditBalanceChange(val showEditBalance: Boolean) : HomeEvent()
    data class OnNewBalanceChange(val newBalance: String) : HomeEvent()
    data class OnAddLastTransaction(val lastTransaction: LastTransaction) : HomeEvent()
    data class OnAddSavingPlan(val savingPlan: SavingPlan) : HomeEvent()
    data class OnLastTransactionDelete(val lastTransaction: LastTransaction) : HomeEvent()
    data class OnSavingPlanDelete(val savingPlan: SavingPlan) : HomeEvent()
}