package com.commercial.bills.ui.screen.home.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.data.repository.BalanceRepository
import com.commercial.bills.data.repository.LastTransactionRepository
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
class HomeViewModel @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val savingPlanRepository: SavingPlanRepository,
    private val lastTransactionRepository: LastTransactionRepository
) : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state
    private var getBalanceJob: Job? = null
    private var getLastTransactionsJob: Job? = null
    private var getSavingPlansJob: Job? = null

    init {
        getBalance()
        getLastTransactions()
        getSavingPlans()
    }

    fun onEvent(homeEvent: HomeEvent) {
        viewModelScope.launch {
            when(homeEvent){
                is HomeEvent.OnBalanceChange -> {
                    changeBalance(homeEvent.newBalance)
                }
                is HomeEvent.OnNewBalanceChange -> {
                    _state.value = state.value.copy(
                        newBalance = homeEvent.newBalance
                    )
                }
                is HomeEvent.OnShowEditBalanceChange -> {
                    _state.value = state.value.copy(
                        showBalanceEdit = homeEvent.showEditBalance,
                        newBalance = ""
                    )
                }
                is HomeEvent.OnLastTransactionDelete -> {
                    deleteLastTransaction(homeEvent.lastTransaction)
                }
                is HomeEvent.OnSavingPlanDelete -> {
                    deleteSavingPlan(homeEvent.savingPlan)
                }
                is HomeEvent.OnAddLastTransaction -> {
                    insertLastTransaction(homeEvent.lastTransaction)
                }
                is HomeEvent.OnAddSavingPlan -> {
                    insertSavingPlan(homeEvent.savingPlan)
                }
            }
        }
    }

    private fun deleteLastTransaction(lastTransaction: LastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            lastTransactionRepository.deleteLastTransaction(lastTransaction)
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

    private fun deleteSavingPlan(savingPlan: SavingPlan) {
        viewModelScope.launch(Dispatchers.IO) {
            savingPlanRepository.deleteSavingPlan(savingPlan)
        }
    }

    private fun changeBalance(newBalance: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            balanceRepository.decreaseBalance(balanceRepository.getBalance().balance)
            balanceRepository.increaseBalance(newBalance)
        }
    }

    private fun getBalance() {
        getBalanceJob?.cancel()

        getBalanceJob =
            balanceRepository
                .getBalanceFlow()
                .flowOn(Dispatchers.IO)
                .onEach { balance ->
                    if(balance == null) {
                        balanceRepository.insertBalance()
                    }
                    _state.value = state.value.copy(
                        balance = balance
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
}
