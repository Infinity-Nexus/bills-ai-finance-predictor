package com.commercial.bills.ui.screen.prediction.control

import androidx.compose.runtime.Stable

@Stable
sealed class PredictionEvent {
    data class OnAdStatusChange(val adStatus: Boolean) : PredictionEvent()
    data class OnShowAdChange(val showAd: Boolean) : PredictionEvent()
    data class OnShowPredictionScreenChange(val showPredictionScreen: Boolean) : PredictionEvent()
    data class OnIncomeAmountChange(val newAmount: Float) : PredictionEvent()
    data class OnExpensesAmountChange(val newAmount: Float) : PredictionEvent()
    data class OnSelectedChartRangeIndexChange(val newIndex: Int) : PredictionEvent()
}