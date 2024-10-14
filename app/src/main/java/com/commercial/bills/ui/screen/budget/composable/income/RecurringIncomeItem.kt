package com.commercial.bills.ui.screen.budget.composable.income

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.ui.util.composable.IsDebitIndicatorCard

@Composable
fun RecurringIncomeItem(
    modifier: Modifier = Modifier,
    recurringTransaction: () -> RecurringTransaction,
    contentPadding: Dp
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = contentPadding,
                    top = contentPadding / 2,
                    end = contentPadding / 2,
                    bottom = contentPadding / 2
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = recurringTransaction().name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                softWrap = true
            )

            Spacer(modifier = Modifier.width(contentPadding))

            IsDebitIndicatorCard(
                amount = { recurringTransaction().amount },
                isDebitIconRotation = 0f,
                isDebitElementColor = Color.Green,
                contentPadding = contentPadding
            )
        }
    }
}