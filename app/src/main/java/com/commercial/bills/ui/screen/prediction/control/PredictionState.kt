package com.commercial.bills.ui.screen.prediction.control

import javax.annotation.concurrent.Immutable

@Immutable
data class PredictionState(
    val adStatus: Boolean = false,
    val showAd: Boolean = false,
    val showPredictionScreen: Boolean = false,
    val incomeAmount: Float = 0f,
    val expensesAmount: Float = 0f,
    val selectedChartRangeIndex: Int = -1,
    val showPredictionSection: Boolean = false,
    val incomePastValues: List<Float> = emptyList(),
    val expensesPastValues: List<Float> = emptyList()
)