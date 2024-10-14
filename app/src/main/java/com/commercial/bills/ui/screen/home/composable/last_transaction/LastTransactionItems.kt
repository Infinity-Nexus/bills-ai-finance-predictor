package com.commercial.bills.ui.screen.home.composable.last_transaction

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
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.ui.util.composable.CustomStickyHeader
import com.commercial.bills.ui.util.function.formatDate
import com.commercial.bills.ui.util.composable.screenFractionDp

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.lastTransactionItems(
    onLastTransactionDelete: (LastTransaction) -> Unit,
    lastTransactions: () -> List<LastTransaction>,
    contentPadding: Dp
) {
    val lastTransactionsGroupedByDate = lastTransactions().groupBy { lastTransaction ->
        formatDate(date = lastTransaction.date)
    }
    val maxMapIndex = lastTransactionsGroupedByDate.count() - 1

    lastTransactionsGroupedByDate.onEachIndexed { mapIndex, (dateGroup, lastTransactions) ->
        stickyHeader {
            CustomStickyHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = contentPadding),
                title = dateGroup
            )
        }

        itemsIndexed(
            items = lastTransactions,
            key = { _, lastTransaction ->
                lastTransaction.id
            }
        ) { listIndex, lastTransaction ->

            Row(verticalAlignment = Alignment.Top) {
                LastTransactionItem(
                    modifier = Modifier
                        .weight(0.9f)
                        .height(
                            screenFractionDp(
                                fraction = 0.1f,
                                isWidth = false
                            )
                        ),
                    lastTransaction = { lastTransaction },
                    contentPadding = contentPadding
                )

                IconButton(
                    onClick = {
                        onLastTransactionDelete(lastTransaction)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }

            val maxListIndex = lastTransactions.count() - 1

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