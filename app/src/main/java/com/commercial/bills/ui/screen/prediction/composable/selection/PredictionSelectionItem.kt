package com.commercial.bills.ui.screen.prediction.composable.selection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.commercial.bills.ui.screen.prediction.util.PredictionSelectionItemData
import com.commercial.bills.ui.util.function.formatCurrency

@Composable
fun PredictionSelectionItem(
    modifier: Modifier = Modifier,
    predictionSelectionItemData: PredictionSelectionItemData,
    amount: () -> Float,
    contentPadding: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                modifier = Modifier.graphicsLayer {
                    this.rotationZ = predictionSelectionItemData.iconRotation
                },
                painter = painterResource(id = predictionSelectionItemData.icon),
                tint = predictionSelectionItemData.color,
                contentDescription = null
            )
            Text(
                text = stringResource(id = predictionSelectionItemData.title),
                style = MaterialTheme.typography.bodyLarge,
                color = predictionSelectionItemData.color
            )
        }
        Text(
            text = formatCurrency(amount()),
            style = MaterialTheme.typography.headlineSmall,
            color = predictionSelectionItemData.color
        )
    }
}