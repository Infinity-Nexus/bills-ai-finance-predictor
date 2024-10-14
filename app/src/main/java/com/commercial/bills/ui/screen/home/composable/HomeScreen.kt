package com.commercial.bills.ui.screen.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commercial.bills.R
import com.commercial.bills.data.entity.Balance
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.ui.theme.PaddingSizes
import com.commercial.bills.ui.util.composable.CustomHeadLine
import com.commercial.bills.ui.util.composable.CustomLazyColumn
import com.commercial.bills.ui.util.composable.CustomLazyRow
import com.commercial.bills.ui.screen.home.composable.last_transaction.lastTransactionItems
import com.commercial.bills.ui.screen.home.composable.saving_plan.savingPlanItems
import com.commercial.bills.ui.screen.home.control.HomeEvent
import com.commercial.bills.ui.screen.home.control.HomeState
import com.commercial.bills.ui.util.composable.screenFractionDp
import com.commercial.bills.ui.util.function.formatCurrency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeState: () -> HomeState,
    onEvent: (HomeEvent) -> Unit,
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
        BalanceSection(
            balance = { homeState().balance },
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content,
            showEditBalance = { homeState().showBalanceEdit },
            onBalanceChange = { newBalance ->
                onEvent(HomeEvent.OnBalanceChange(newBalance))
            },
            onShowEditBalanceChange = { showEditBalance ->
                onEvent(HomeEvent.OnShowEditBalanceChange(showEditBalance))
            },
            newBalance = { homeState().newBalance },
            onNewBalanceChange = { newBalance ->
                onEvent(HomeEvent.OnNewBalanceChange(newBalance))
            }
        )
        SavingPlansSection(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenFractionDp(fraction = 0.45f))
                .padding(bottom = PaddingSizes.Screen),
            savingPlans = { homeState().savingPlans },
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content,
            onSavingPlanDelete = { savingPlan ->
                onEvent(HomeEvent.OnSavingPlanDelete(savingPlan))

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
                            onEvent(HomeEvent.OnAddSavingPlan(savingPlan))
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
            }
        )
        LastTransactionsSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = PaddingSizes.Screen),
            lastTransactions = { homeState().lastTransactions },
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content,
            onLastTransactionDelete = { lastTransaction ->
                onEvent(HomeEvent.OnLastTransactionDelete(lastTransaction))

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
                            onEvent(HomeEvent.OnAddLastTransaction(lastTransaction))
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
            }
        )
    }
}

@Composable
fun BalanceSection(
    balance: () -> Balance?,
    onBalanceChange: (Float) -> Unit,
    onShowEditBalanceChange: (Boolean) -> Unit,
    showEditBalance: () -> Boolean,
    newBalance: () -> String,
    onNewBalanceChange: (String) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    Row(
        modifier = Modifier.padding(bottom = contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.balance_headline_name),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.width(contentPadding))

        IconButton(
            onClick = {
                onShowEditBalanceChange(true)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "edit"
            )
        }
    }

    EditBalance(
        onShowEditBalanceChange = onShowEditBalanceChange,
        onBalanceChange = onBalanceChange,
        onNewBalanceChange = onNewBalanceChange,
        newBalance = { newBalance() },
        showEditBalance = { showEditBalance() },
        screenPadding = PaddingSizes.Screen,
        contentPadding = PaddingSizes.Content
    )

    BalanceText(
        balance = { balance() },
        screenPadding = screenPadding,
        showEditBalance = { showEditBalance() }
    )
}

@Composable
fun BalanceText(
    balance: () -> Balance?,
    screenPadding: Dp,
    showEditBalance: () -> Boolean
) {
    if(showEditBalance()) return

    Text(
        modifier = Modifier.padding(bottom = screenPadding),
        text = "${balance()?.balance?.let { formatCurrency(it) }}",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun EditBalance(
    onShowEditBalanceChange: (Boolean) -> Unit,
    onBalanceChange: (Float) -> Unit,
    showEditBalance: () -> Boolean,
    newBalance: () -> String,
    onNewBalanceChange: (String) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    if(!showEditBalance()) return

    val isNewBalanceError = when(newBalance().toFloatOrNull()) {
        null -> { true }
        else -> { false }
    }

    Row(
        modifier = Modifier.padding(bottom = screenPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            label = {
                Text(text = "New Balance")
            },
            value = newBalance(),
            onValueChange = onNewBalanceChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            isError = isNewBalanceError
        )

        Spacer(modifier = Modifier.width(screenPadding))

        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    if(!isNewBalanceError) {
                        onBalanceChange(newBalance().toFloat())
                        onShowEditBalanceChange(false)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_confirm),
                    contentDescription = "confirm"
                )
            }

            Spacer(modifier = Modifier.height(contentPadding))

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    onShowEditBalanceChange(false)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "cancel"
                )
            }
        }
    }
}

@Composable
fun SavingPlansSection(
    modifier: Modifier = Modifier,
    savingPlans: () -> List<SavingPlan>,
    onSavingPlanDelete: (SavingPlan) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val savingPlanItemWidth = screenFractionDp(fraction = 0.5f)

    CustomLazyRow(modifier = modifier,
        headline = {
            CustomHeadLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = screenPadding,
                        end = screenPadding,
                        bottom = contentPadding
                    ),
                text = stringResource(id = R.string.saving_plans_list_name)
            )
        },
        screenPadding = screenPadding,
        contentPadding = contentPadding,
        items = { savingPlans() },
        itemComposables = { savingPlansItems, _, _ ->
            savingPlanItems(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(savingPlanItemWidth),
                savingPlans = { savingPlansItems() },
                screenPadding = screenPadding,
                contentPadding = contentPadding,
                onSavingPlanDelete = onSavingPlanDelete
            )
        }
    )
}

@Composable
fun LastTransactionsSection(
    modifier: Modifier = Modifier,
    lastTransactions: () -> List<LastTransaction>,
    onLastTransactionDelete: (LastTransaction) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    CustomLazyColumn(
        modifier = modifier,
        headline = {
            CustomHeadLine(text = stringResource(id = R.string.last_transactions_list_name))
        },
        items = { lastTransactions() },
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

