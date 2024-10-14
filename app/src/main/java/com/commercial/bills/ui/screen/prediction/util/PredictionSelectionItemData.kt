package com.commercial.bills.ui.screen.prediction.util

import androidx.compose.ui.graphics.Color
import javax.annotation.concurrent.Immutable

@Immutable
data class PredictionSelectionItemData(
    val title: Int,
    val icon: Int,
    val iconRotation: Float,
    val color: Color
)

