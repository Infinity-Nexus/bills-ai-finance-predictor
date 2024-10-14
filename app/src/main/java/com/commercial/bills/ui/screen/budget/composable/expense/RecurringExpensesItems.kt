package com.commercial.bills.ui.screen.budget.composable.expense

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commercial.bills.R
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.ui.util.composable.CustomStickyHeader
import com.commercial.bills.ui.util.function.formatRecurringType
import com.commercial.bills.ui.util.composable.screenFractionDp
import com.commercial.bills.ui.util.function.formatCurrency

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.recurringExpensesItems(
    recurringTransactions: () -> List<RecurringTransaction>,
    onRecurringTransactionDelete: (RecurringTransaction) -> Unit,
    contentPadding: Dp
) {
    val recurringTransactionsGroupedByRecurringType = recurringTransactions()
        .filter { recurringTransaction ->
            recurringTransaction.isDebit
        }
        .groupBy { recurringTransaction ->
            formatRecurringType(recurringTransaction.recurringType)
        }
    val maxMapIndex = recurringTransactionsGroupedByRecurringType.count() - 1

    recurringTransactionsGroupedByRecurringType.onEachIndexed { mapIndex, (recurringTypeGroup, recurringTransactions) ->
        stickyHeader {
            val recurringTypeGroupSum = recurringTransactions.sumOf { recurringTransaction ->
                recurringTransaction.amount.toDouble()
            }.toFloat()
            val stickyHeaderFormat = "$recurringTypeGroup: ${formatCurrency(recurringTypeGroupSum)}"

            CustomStickyHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = contentPadding),
                title = stickyHeaderFormat
            )
        }

        itemsIndexed(
            items = recurringTransactions,
            key = { _, recurringTransaction ->
                recurringTransaction.id
            }
        ) { listIndex, recurringTransaction ->
            Row(verticalAlignment = Alignment.Top) {
                RecurringExpensesItem(
                    modifier = Modifier
                        .weight(0.9f)
                        .height(
                            screenFractionDp(
                                fraction = 0.1f,
                                isWidth = false
                            )
                        ),
                    recurringTransaction = { recurringTransaction },
                    contentPadding = contentPadding
                )

                IconButton(
                    onClick = { onRecurringTransactionDelete(recurringTransaction) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }

            val maxListIndex = recurringTransactions.count() - 1

            val bottomPadding =
                if(listIndex != maxListIndex)
                    contentPadding
                else if(mapIndex == maxMapIndex)
                    contentPadding
                else
                    0.dp

            Spacer(modifier = Modifier.height(bottomPadding))
        }
    }
}