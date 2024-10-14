package com.commercial.bills.ui.screen.home.composable.saving_plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.ui.util.composable.CustomLinearProgressIndicator
import com.commercial.bills.ui.util.function.formatCurrency

@Composable
fun SavingPlanItem(
    modifier: Modifier = Modifier,
    savingPlan: () -> SavingPlan,
    contentPadding: Dp
) {
    Row(modifier = modifier) {
        Card(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = savingPlan().name,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        softWrap = true
                    )

                    Spacer(modifier = Modifier.width(contentPadding))

                    Text(
                        text = "-" + formatCurrency(
                            savingPlan().amountToSave()
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        softWrap = true
                    )
                }

                CustomLinearProgressIndicator(
                    goal = { savingPlan().amount },
                    progress = { savingPlan().savedAmount }
                )
            }
        }
    }
}
