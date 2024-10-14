package com.commercial.bills.ui.screen.budget.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.commercial.bills.R
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.ui.screen.budget.composable.expense.recurringExpensesItems
import com.commercial.bills.ui.screen.budget.composable.income.recurringIncomeItems
import com.commercial.bills.ui.screen.budget.control.BudgetEvent
import com.commercial.bills.ui.screen.budget.control.BudgetState
import com.commercial.bills.ui.theme.PaddingSizes
import com.commercial.bills.ui.util.composable.CustomLazyColumn
import com.commercial.bills.ui.util.composable.CustomTabRow
import com.commercial.bills.ui.screen.budget.util.BudgetTabRowItems
import com.commercial.bills.ui.util.function.formatCurrency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BudgetScreen(
    budgetState: () -> BudgetState,
    onEvent: (BudgetEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = PaddingSizes.Screen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SavingsSection(
            savingPlans = { budgetState().savingPlans },
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content
        )

        TabRowSection(
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content
        ) { selectedPageIndex ->
            when(selectedPageIndex()) {
                0 -> {
                    IncomeTab(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = PaddingSizes.Screen),
                        recurringTransactions = { budgetState().recurringTransactions },
                        screenPadding = PaddingSizes.Screen,
                        contentPadding = PaddingSizes.Content,
                        onRecurringTransactionDelete = { recurringTransaction ->
                            onEvent(BudgetEvent.OnRecurringTransactionDelete(recurringTransaction))

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
                                        onEvent(BudgetEvent.OnAddRecurringTransaction(recurringTransaction))
                                    }
                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        }
                    )
                }
                1 -> {
                    ExpensesTab(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = PaddingSizes.Screen),
                        recurringTransactions = { budgetState().recurringTransactions },
                        screenPadding = PaddingSizes.Screen,
                        contentPadding = PaddingSizes.Content,
                        onRecurringTransactionDelete = { recurringTransaction ->
                            onEvent(BudgetEvent.OnRecurringTransactionDelete(recurringTransaction))

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
                                        onEvent(BudgetEvent.OnAddRecurringTransaction(recurringTransaction))
                                    }
                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SavingsSection(
    savingPlans: () -> List<SavingPlan>,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val savings = savingPlans()
        .map { savingPlan -> savingPlan.savedAmount }
        .sum()

    Text(
        modifier = Modifier.padding(bottom = contentPadding),
        text = stringResource(id = R.string.savings_headline_name),
        style = MaterialTheme.typography.headlineLarge
    )
    Text(
        modifier = Modifier.padding(bottom = screenPadding),
        text = formatCurrency(savings),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun TabRowSection(
    screenPadding: Dp,
    contentPadding: Dp,
    tabs: @Composable (() -> Int) -> Unit,
) {
    CustomTabRow(
        items = { BudgetTabRowItems.items },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        tabs = tabs
    )
}

@Composable
fun IncomeTab(
    modifier: Modifier = Modifier,
    recurringTransactions: () -> List<RecurringTransaction>,
    onRecurringTransactionDelete: (RecurringTransaction) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    CustomLazyColumn(
        modifier = modifier,
        headline = {},
        items = { recurringTransactions() },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        itemComposables = { recurringTransactionsItems, _, _ ->
            recurringIncomeItems(
                recurringTransactions = { recurringTransactionsItems() },
                contentPadding = contentPadding,
                onRecurringTransactionDelete = onRecurringTransactionDelete
            )
        }
    )
}

@Composable
fun ExpensesTab(
    modifier: Modifier = Modifier,
    recurringTransactions: () -> List<RecurringTransaction>,
    onRecurringTransactionDelete: (RecurringTransaction) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
)  {
    CustomLazyColumn(
        modifier = modifier,
        headline = {},
        items = { recurringTransactions() },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        itemComposables = { recurringTransactionsItems, _, _ ->
            recurringExpensesItems(
                recurringTransactions = { recurringTransactionsItems() },
                contentPadding = contentPadding,
                onRecurringTransactionDelete = onRecurringTransactionDelete
            )
        }
    )
}