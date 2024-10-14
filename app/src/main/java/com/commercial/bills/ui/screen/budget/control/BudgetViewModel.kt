package com.commercial.bills.ui.screen.budget.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.repository.RecurringTransactionRepository
import com.commercial.bills.data.repository.SavingPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val savingPlanRepository: SavingPlanRepository,
    private val recurringTransactionRepository: RecurringTransactionRepository
) : ViewModel() {
    private val _state = mutableStateOf(BudgetState())
    val state: State<BudgetState> = _state
    private var getSavingPlansJob: Job? = null
    private var getRecurringTransactionsJob: Job? = null

    init {
        getSavingPlans()
        getRecurringTransactions()
    }

    fun onEvent(budgetEvent: BudgetEvent) {
        viewModelScope.launch {
            when(budgetEvent) {
                is BudgetEvent.OnRecurringTransactionDelete -> {
                    deleteRecurringTransaction(budgetEvent.recurringTransaction)
                }
                is BudgetEvent.OnAddRecurringTransaction -> {
                    insertRecurringTransaction(budgetEvent.recurringTransaction)
                }
            }
        }
    }

    private fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            recurringTransactionRepository.deleteRecurringTransaction(
                recurringTransaction
            )
        }
    }

    private fun insertRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            recurringTransactionRepository.insertRecurringTransaction(
                recurringTransaction
            )
        }
    }

    private fun getSavingPlans() {
        getSavingPlansJob?.cancel()

        getSavingPlansJob =
            savingPlanRepository
                .getSavingPlans()
                .flowOn(Dispatchers.IO)
                .onEach { savingPlans ->
                    _state.value = state.value.copy(
                        savingPlans = savingPlans
                    )
                }
                .launchIn(viewModelScope)
    }

    private fun getRecurringTransactions() {
        getRecurringTransactionsJob?.cancel()

        getRecurringTransactionsJob =
            recurringTransactionRepository
                .getRecurringTransactions()
                .flowOn(Dispatchers.IO)
                .onEach { recurringTransactions ->
                    _state.value = state.value.copy(
                        recurringTransactions = recurringTransactions
                    )
                }
                .launchIn(viewModelScope)
    }
}
