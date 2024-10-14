package com.commercial.bills.ui.screen.prediction.composable.selection

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.commercial.bills.R
import com.commercial.bills.ui.screen.prediction.util.PredictionSelectionItemData

@Composable
fun PredictionSelectionItems(
    modifier: Modifier,
    incomeAmount: () -> Float,
    expensesAmount: () -> Float,
    contentPadding: Dp
) {
    val predictionSelectionItemsData = listOf(
        PredictionSelectionItemData(
            title = R.string.prediction_selection_item_income_name,
            icon = R.drawable.ic_prediction,
            iconRotation = 0f,
            color = Color.Green
        ),
        PredictionSelectionItemData(
            title = R.string.prediction_selection_item_expenses_name,
            icon = R.drawable.ic_prediction,
            iconRotation = -180f,
            color = Color.Red
        )
    )
    val maxItemsIndex = predictionSelectionItemsData.count() - 1

    predictionSelectionItemsData.forEachIndexed { index, predictionSelectionItemData ->
        val nextAmount: Float = when(index) {
            0 -> incomeAmount()
            1 -> expensesAmount()
            else -> 0f
        }

        PredictionSelectionItem(
            modifier = modifier,
            predictionSelectionItemData = predictionSelectionItemData,
            amount = { nextAmount },
            contentPadding = contentPadding
        )

        if(index != maxItemsIndex)
            Spacer(modifier = Modifier.width(contentPadding))
    }
}