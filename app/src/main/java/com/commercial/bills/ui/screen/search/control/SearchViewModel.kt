package com.commercial.bills.ui.screen.search.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.data.repository.BalanceRepository
import com.commercial.bills.data.repository.LastTransactionRepository
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
class SearchViewModel @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val lastTransactionRepository: LastTransactionRepository,
    private val recurringTransactionRepository: RecurringTransactionRepository,
    private val savingPlanRepository: SavingPlanRepository
) : ViewModel() {
    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state
    private var getLastTransactionsJob: Job? = null
    private var getRecurringTransactionsJob: Job? = null
    private var getSavingPlansJob: Job? = null

    init {
        getLastTransactions()
        getRecurringTransactions()
        getSavingPlans()
    }

    fun onEvent(searchEvent: SearchEvent) {
        viewModelScope.launch {
            when(searchEvent) {
                is SearchEvent.OnSearchTextChange -> {
                    _state.value = state.value.copy(
                        searchText = searchEvent.newText
                    )
                }
                is SearchEvent.OnLastTransactionDelete -> {
                    deleteLastTransaction(searchEvent.lastTransaction)
                }
                is SearchEvent.OnSavingPlanDelete -> {
                    deleteSavingPlan(searchEvent.savingPlan)
                }
                is SearchEvent.OnRecurringTransactionDelete -> {
                    deleteRecurringTransaction(searchEvent.recurringTransaction)
                }
                is SearchEvent.OnAddLastTransaction -> {
                    insertLastTransaction(searchEvent.lastTransaction)
                }
                is SearchEvent.OnAddSavingPlan -> {
                    insertSavingPlan(searchEvent.savingPlan)
                }
                is SearchEvent.OnAddRecurringTransaction -> {
                    insertRecurringTransaction(searchEvent.recurringTransaction)
                }
            }
        }
    }

    private fun insertSavingPlan(savingPlan: SavingPlan) {
        viewModelScope.launch(Dispatchers.IO) {
            savingPlanRepository.insertSavingPlan(savingPlan)
        }
    }

    private fun insertLastTransaction(lastTransaction: LastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            lastTransactionRepository.insertLastTransaction(lastTransaction)
            when(lastTransaction.isDebit) {
                true -> {
                    balanceRepository.decreaseBalance(lastTransaction.amount)
                }
                false -> {
                    balanceRepository.increaseBalance(lastTransaction.amount)
                }
            }
        }
    }

    private fun insertRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            recurringTransactionRepository.insertRecurringTransaction(
                recurringTransaction
            )
        }
    }

    private fun deleteLastTransaction(lastTransaction: LastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            lastTransactionRepository.deleteLastTransaction(
                lastTransaction
            )
            when(lastTransaction.isDebit) {
                true -> {
                    balanceRepository.increaseBalance(lastTransaction.amount)
                }
                false -> {
                    balanceRepository.decreaseBalance(lastTransaction.amount)
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

    private fun deleteSavingPlan(savingPlan: SavingPlan) {
        viewModelScope.launch(Dispatchers.IO) {
            savingPlanRepository.deleteSavingPlan(
                savingPlan
            )
        }
    }

    private fun getLastTransactions() {
        getLastTransactionsJob?.cancel()

        getLastTransactionsJob =
            lastTransactionRepository
                .getLastTransactions()
                .flowOn(Dispatchers.IO)
                .onEach { lastTransactions ->
                    _state.value = state.value.copy(
                        lastTransactions = lastTransactions
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
}
