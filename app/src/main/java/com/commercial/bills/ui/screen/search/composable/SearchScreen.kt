package com.commercial.bills.ui.screen.search.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.commercial.bills.R
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.ui.screen.home.composable.last_transaction.lastTransactionItems
import com.commercial.bills.ui.screen.home.composable.saving_plan.savingPlanItems
import com.commercial.bills.ui.screen.search.control.SearchEvent
import com.commercial.bills.ui.screen.search.control.SearchState
import com.commercial.bills.ui.theme.PaddingSizes
import com.commercial.bills.ui.util.composable.CustomHeadLine
import com.commercial.bills.ui.util.composable.CustomLazyColumn
import com.commercial.bills.ui.util.composable.CustomLazyRow
import com.commercial.bills.ui.util.composable.screenFractionDp
import com.commercial.bills.ui.util.function.colorBlend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    searchState: () -> SearchState,
    onEvent: (SearchEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = PaddingSizes.Screen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SearchSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingSizes.Screen),
            searchText = { searchState().searchText },
            onSearchTextChange = { newText ->
                onEvent(SearchEvent.OnSearchTextChange(newText))
            }
        )

        Spacer(modifier = Modifier.height(PaddingSizes.Content))

        SearchResultsSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            searchText = { searchState().searchText },
            lastTransactions = { searchState().lastTransactions },
            recurringTransactions = { searchState().recurringTransactions },
            savingPlans = { searchState().savingPlans },
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content,
            onLastTransactionDelete = { lastTransaction ->
                onEvent(SearchEvent.OnLastTransactionDelete(lastTransaction))

                coroutineScope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "${lastTransaction.name} Deleted!",
                            actionLabel = "Undo",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )

                    when(result) {
                        SnackbarResult.ActionPerformed -> {
                            onEvent(SearchEvent.OnAddLastTransaction(lastTransaction))
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
            },
            onSavingPlanDelete = { savingPlan ->
                onEvent(SearchEvent.OnSavingPlanDelete(savingPlan))

                coroutineScope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "${savingPlan.name} Deleted!",
                            actionLabel = "Undo",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )

                    when(result) {
                        SnackbarResult.ActionPerformed -> {
                            onEvent(SearchEvent.OnAddSavingPlan(savingPlan))
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
            },
            onRecurringTransactionDelete = { recurringTransaction ->
                onEvent(SearchEvent.OnRecurringTransactionDelete(recurringTransaction))

                coroutineScope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "${recurringTransaction.name} Deleted!",
                            actionLabel = "Undo",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )

                    when(result) {
                        SnackbarResult.ActionPerformed -> {
                            onEvent(SearchEvent.OnAddRecurringTransaction(recurringTransaction))
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
            }
        )
    }
}

@Composable
fun SearchSection(
    modifier: Modifier = Modifier,
    searchText: () -> String,
    onSearchTextChange: (String) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorBlend(
                color1 = MaterialTheme.colorScheme.onBackground,
                color2 = Color.Transparent,
                intensity = 0.5f
            )
        )
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText(),
            onValueChange = onSearchTextChange,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = "Search Database...",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SearchResultsSection(
    modifier: Modifier = Modifier,
    searchText: () -> String,
    lastTransactions: () -> List<LastTransaction>,
    onLastTransactionDelete: (LastTransaction) -> Unit,
    recurringTransactions: () -> List<RecurringTransaction>,
    onRecurringTransactionDelete: (RecurringTransaction) -> Unit,
    savingPlans: () -> List<SavingPlan>,
    onSavingPlanDelete: (SavingPlan) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    if(searchText().isBlank()) return

    LastTransactionsResult(
        onLastTransactionDelete = onLastTransactionDelete,
        modifier = modifier,
        searchText = { searchText() },
        lastTransactions = { lastTransactions() },
        screenPadding = screenPadding,
        contentPadding = contentPadding
    )

    RecurringTransactionsResult(
        onRecurringTransactionDelete = onRecurringTransactionDelete,
        modifier = modifier,
        searchText = { searchText() },
        recurringTransactions = { recurringTransactions() },
        screenPadding = screenPadding,
        contentPadding = contentPadding
    )

    SavingPlansResult(
        onSavingPlanDelete = onSavingPlanDelete,
        modifier = modifier,
        searchText = { searchText() },
        savingPlans = { savingPlans() },
        screenPadding = screenPadding,
        contentPadding = contentPadding
    )
}

@Composable
fun SavingPlansResult(
    onSavingPlanDelete: (SavingPlan) -> Unit,
    modifier: Modifier = Modifier,
    searchText: () -> String,
    savingPlans: () -> List<SavingPlan>,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val newSavingPlans = savingPlans().filter { savingPlan ->
        savingPlan.name.lowercase().contains(searchText().lowercase())
    }

    if(newSavingPlans.isEmpty()) return

    val savingPlanItemWidth = screenFractionDp(fraction = 0.5f)
    val savingPlanItemHeight = screenFractionDp(fraction = 0.28f)

    CustomLazyRow(
        modifier = modifier.padding(top = contentPadding),
        headline = {
            CustomHeadLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = screenPadding,
                        end = screenPadding,
                        bottom = contentPadding
                    ),
                text = stringResource(id = R.string.saving_plans_list_name),
                textStyle = MaterialTheme.typography.titleSmall
            )
        },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        items = { newSavingPlans },
        itemComposables = { savingPlansItems, _, _ ->
            savingPlanItems(
                modifier = Modifier
                    .width(savingPlanItemWidth)
                    .height(savingPlanItemHeight),
                savingPlans = { savingPlansItems() },
                screenPadding = screenPadding,
                contentPadding = contentPadding,
                onSavingPlanDelete = onSavingPlanDelete
            )
        }
    )
}

@Composable
fun LastTransactionsResult(
    onLastTransactionDelete: (LastTransaction) -> Unit,
    modifier: Modifier = Modifier,
    searchText: () -> String,
    lastTransactions: () -> List<LastTransaction>,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val newLastTransactions = lastTransactions().filter { lastTransaction ->
        lastTransaction.name.lowercase().contains(searchText().lowercase())
    }

    if(newLastTransactions.isEmpty()) return

    CustomLazyColumn(
        modifier = modifier.padding(
            start = screenPadding,
            end = screenPadding,
            top = contentPadding
        ),
        headline = {
            CustomHeadLine(
                text = stringResource(id = R.string.last_transactions_list_name),
                textStyle = MaterialTheme.typography.titleSmall
            )
        },
        items = { newLastTransactions },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        itemComposables = { lastTransactionsItems, _, _ ->
            lastTransactionItems(
                lastTransactions = { lastTransactionsItems() },
                contentPadding = contentPadding,
                onLastTransactionDelete = onLastTransactionDelete
            )
        }
    )
}

@Composable
fun RecurringTransactionsResult(
    onRecurringTransactionDelete: (RecurringTransaction) -> Unit,
    modifier: Modifier = Modifier,
    searchText: () -> String,
    recurringTransactions: () -> List<RecurringTransaction>,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val newRecurringTransactions = recurringTransactions().filter { recurringTransaction ->
        recurringTransaction.name.lowercase().contains(searchText().lowercase())
    }

    if(newRecurringTransactions.isEmpty()) return

    CustomLazyColumn(
        modifier = modifier.padding(
            start = screenPadding,
            end = screenPadding,
            top = contentPadding
        ),
        headline = {
            CustomHeadLine(
                text = stringResource(id = R.string.recurring_transactions_list_name),
                textStyle = MaterialTheme.typography.titleSmall
            )
        },
        items = { newRecurringTransactions },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        itemComposables = { recurringTransactionsItems, _, _ ->
            recurringTransactionItems(
                recurringTransactions = { recurringTransactionsItems() },
                contentPadding = contentPadding,
                onRecurringTransactionDelete = onRecurringTransactionDelete
            )
        }
    )
}