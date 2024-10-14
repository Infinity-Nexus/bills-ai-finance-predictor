package com.commercial.bills.ui.util.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import com.commercial.bills.R
import com.commercial.bills.ui.util.function.formatCurrency

@Composable
fun IsDebitIndicatorCard(
    amount: () -> Float,
    isDebitIconRotation: Float,
    isDebitElementColor: Color,
    contentPadding: Dp
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(screenFractionDp(fraction = 0.4f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon (
                modifier = Modifier.graphicsLayer {
                    this.rotationZ = isDebitIconRotation
                },
                painter = painterResource(id = R.drawable.ic_debit_or_credit),
                tint = isDebitElementColor,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(contentPadding / 2))
            Text(
                text = formatCurrency(amount()),
                style = MaterialTheme.typography.titleSmall,
                color = isDebitElementColor,
                maxLines = 1,
                softWrap = true
            )
        }
    }
}