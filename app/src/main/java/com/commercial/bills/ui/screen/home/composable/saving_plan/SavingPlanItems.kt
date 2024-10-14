package com.commercial.bills.ui.screen.home.composable.saving_plan

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commercial.bills.R
import com.commercial.bills.data.entity.SavingPlan

fun LazyListScope.savingPlanItems(
    modifier: Modifier = Modifier,
    onSavingPlanDelete: (SavingPlan) -> Unit,
    savingPlans: () -> List<SavingPlan>,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val maxItemsIndex = savingPlans().count() - 1

    itemsIndexed(
        items = savingPlans(),
        key = { _, savingPlan ->
            savingPlan.id
        }
    ) { index, savingPlan ->
        if(index == 0)
            Spacer(modifier = Modifier.width(screenPadding))

        Row(verticalAlignment = Alignment.Top) {
            SavingPlanItem(
                modifier = modifier,
                savingPlan = { savingPlan },
                contentPadding = contentPadding
            )

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { onSavingPlanDelete(savingPlan) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )
            }
        }

        val endPadding = when(index == maxItemsIndex) {
            true -> screenPadding
            false -> contentPadding
        }

        Spacer(modifier = Modifier.width(endPadding))
    }
}