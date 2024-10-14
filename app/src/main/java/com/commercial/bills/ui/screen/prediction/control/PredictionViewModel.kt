package com.commercial.bills.ui.screen.prediction.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commercial.bills.data.repository.LastTransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PredictionViewModel @Inject constructor(
    private val lastTransactionRepository: LastTransactionRepository
) : ViewModel() {
    private val _state = mutableStateOf(PredictionState())
    val state: State<PredictionState> = _state
    private var getIncomePastValuesJob: Job? = null
    private var getExpensesPastValuesJob: Job? = null

    init {
        getIncomePastValues()
        getExpensesPastValues()
    }

    fun onEvent(predictionEvent: PredictionEvent) {
        viewModelScope.launch {
            when(predictionEvent) {
                is PredictionEvent.OnAdStatusChange -> {
                    _state.value = state.value.copy(
                        adStatus = predictionEvent.adStatus
                    )
                }
                is PredictionEvent.OnShowAdChange -> {
                    _state.value = state.value.copy(
                        showAd = predictionEvent.showAd
                    )
                }
                is PredictionEvent.OnIncomeAmountChange -> {
                    _state.value = state.value.copy(
                        incomeAmount = predictionEvent.newAmount
                    )
                }
                is PredictionEvent.OnExpensesAmountChange -> {
                    _state.value = state.value.copy(
                        expensesAmount =  predictionEvent.newAmount
                    )
                }
                is PredictionEvent.OnSelectedChartRangeIndexChange -> {
                    _state.value = state.value.copy(
                        selectedChartRangeIndex = predictionEvent.newIndex
                    )
                }
                is PredictionEvent.OnShowPredictionScreenChange -> {
                    _state.value = state.value.copy(
                        showPredictionScreen = predictionEvent.showPredictionScreen
                    )
                }
            }
        }
    }

    private fun getIncomePastValues() {
        getIncomePastValuesJob?.cancel()

        getIncomePastValuesJob =
            lastTransactionRepository
                .getLastTransactions()
                .flowOn(Dispatchers.IO)
                .onEach { lastTransactions ->
                    val summedValuesByMonth  = lastTransactions
                        .filter { lastTransaction -> !lastTransaction.isDebit }
                        .groupBy { lastTransaction -> lastTransaction.date.get(Calendar.MONTH) }
                        .mapValues { (_, transactions) -> transactions.sumOf { it.amount.toDouble() }.toFloat() }

                    _state.value = state.value.copy(
                        incomePastValues = summedValuesByMonth.values.toList().reversed()
                    )
                }
                .launchIn(viewModelScope)
    }

    private fun getExpensesPastValues() {
        getExpensesPastValuesJob?.cancel()

        getExpensesPastValuesJob =
            lastTransactionRepository
                .getLastTransactions()
                .flowOn(Dispatchers.IO)
                .onEach { lastTransactions ->
                    val summedValuesByMonth  = lastTransactions
                        .filter { lastTransaction -> lastTransaction.isDebit }
                        .groupBy { lastTransaction -> lastTransaction.date.get(Calendar.MONTH) }
                        .mapValues { (_, transactions) -> transactions.sumOf { it.amount.toDouble() }.toFloat() }

                    _state.value = state.value.copy(
                        expensesPastValues = summedValuesByMonth.values.toList().reversed()
                    )
                }
                .launchIn(viewModelScope)
    }
}
