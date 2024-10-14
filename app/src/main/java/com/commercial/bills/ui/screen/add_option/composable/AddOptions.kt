package com.commercial.bills.ui.screen.add_option.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commercial.bills.MainActivity
import com.commercial.bills.R
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.data.util.RecurringType
import com.commercial.bills.ui.screen.add_option.control.AddOptionsEvent
import com.commercial.bills.ui.screen.add_option.control.AddOptionsViewModel
import com.commercial.bills.ui.screen.add_option.control.AddRecurringTransactionEvent
import com.commercial.bills.ui.screen.add_option.control.AddRecurringTransactionState
import com.commercial.bills.ui.screen.add_option.control.AddSavingPlanEvent
import com.commercial.bills.ui.screen.add_option.control.AddSavingPlanState
import com.commercial.bills.ui.screen.add_option.control.AddTransactionEvent
import com.commercial.bills.ui.screen.add_option.control.AddTransactionState
import com.commercial.bills.ui.screen.add_option.util.AddOptionType
import com.commercial.bills.ui.theme.PaddingSizes
import com.commercial.bills.ui.util.composable.screenFractionDp
import com.commercial.bills.ui.util.function.formatDate
import java.util.Calendar
import kotlin.math.absoluteValue

@Composable
fun AddOptions(
    addOptionsViewModel: AddOptionsViewModel,
    activity: MainActivity
) {
    val addOptionsState = { addOptionsViewModel.state.value }
    val addOptionsEvent = addOptionsViewModel::addOptionsEvent

    if(!addOptionsState().addOptionsSelected) return

    if(addOptionsState().showBottomSheet) {
        AddOptionsBottomSheet(
            onShowBottomSheetChange = { showBottomSheet ->
                addOptionsEvent(AddOptionsEvent.OnShowBottomSheetChange(showBottomSheet))
            },
            onAddOptionsSelectedChange = { addOptionsSelected ->
                addOptionsEvent(AddOptionsEvent.OnAddOptionsSelected(addOptionsSelected))
            },
            onSelectedAddOptionChange = { newSelectedAddOption ->
                addOptionsEvent(AddOptionsEvent.OnSelectedAddOptionChange(newSelectedAddOption))
            },
            screenPadding = PaddingSizes.Screen,
            contentPadding = PaddingSizes.Content
        )
    }

    when(addOptionsState().selectedAddOption) {
        AddOptionType.TRANSACTION -> {
            AddTransactionScreen(
                addTransactionState = { addOptionsViewModel.addTransactionState.value },
                onEvent = addOptionsViewModel::addTransactionEvent
            )
        }
        AddOptionType.SAVING_PLAN -> {
            AddSavingPlanScreen(
                addSavingPlanState = { addOptionsViewModel.addSavingPlanState.value },
                onEvent = addOptionsViewModel::addSavingPlanEvent
            )
        }
        AddOptionType.RECURRING_TRANSACTION -> {
            AddRecurringTransactionScreen(
                addRecurringTransactionState = { addOptionsViewModel.addRecurringTransactionState.value },
                onEvent = addOptionsViewModel::addRecurringTransactionEvent
            )
        }
        else -> {}
    }
}

@Composable
fun AddTransactionDialog(
    onAddLastTransaction: (LastTransaction) -> Unit,
    onDialogClose: () -> Unit,
    name: () -> String,
    onNameChange: (String) -> Unit,
    amount: () -> String,
    onAmountChange: (String) -> Unit,
    isDebit: () -> Boolean,
    onIsDebitChange: (Boolean) -> Unit,
    onShowDatePickerChange: (Boolean) -> Unit,
    date: () -> Long,
    buttonHeight: Dp,
    screenPadding: Dp
) {
    val isNameError = when(name() == "" ||
        name().toCharArray().count() >= 12) {
        true -> { true }
        false -> { false }
    }
    val isAmountError = when(amount().toFloatOrNull() == null ||
            amount().toFloatOrNull() == 0f) {
        true -> { true }
        false -> { false }
    }
    val interactionSource = remember { MutableInteractionSource() }

    CustomDialog(
        modifier = Modifier
            .size(
                screenFractionDp(
                    fraction = 0.6f,
                    isWidth = false
                )
            ),
        title = stringResource(id = R.string.add_option_transaction_title),
        onConfirm = {
            if(!isNameError && !isAmountError) {
                onAddLastTransaction(
                    LastTransaction(
                        name = name(),
                        amount = amount().toFloat().absoluteValue,
                        isDebit = isDebit(),
                        date = Calendar.getInstance().apply {
                            timeInMillis = date()
                        }
                    )
                )
                onDialogClose()
            }
        },
        onCancel = {
            onDialogClose()
        },
        onDismiss = {
            onDialogClose()
        },
        buttonHeight = buttonHeight,
        screenPadding = screenPadding
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = screenPadding),
            value = name(),
            onValueChange = onNameChange,
            label = {
                Text(text = "Name")
            },
            isError = isNameError
        )
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = screenPadding),
            value = amount(),
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            label = {
                Text(text = "Amount")
            },
            isError = isAmountError
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = screenPadding),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = null
                )
            },
            interactionSource = interactionSource,
            label = {
                Text("Date")
            },
            value = formatDate(
                Calendar
                    .getInstance()
                    .apply { timeInMillis = date() }
            ),
            onValueChange = { },
            readOnly = true
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isDebit(),
                    onClick = {
                        onIsDebitChange(false)
                    }
                )

                Text(
                    text = "Debit",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            )  {
                RadioButton(
                    selected = !isDebit(),
                    onClick = {
                        onIsDebitChange(true)
                    }
                )

                Text(
                    text = "Credit",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if(interactionSource.collectIsPressedAsState().value) {
        onShowDatePickerChange(true)
    }
}

@Composable
fun AddSavingPlanDialog(
    onAddSavingPlan: (SavingPlan) -> Unit,
    onDialogClose: () -> Unit,
    name: () -> String,
    onNameChange: (String) -> Unit,
    amount: () -> String,
    onAmountChange: (String) -> Unit,
    recurringType: () -> String,
    onRecurringTypeChange: (String) -> Unit,
    finishDate: () -> Long,
    isDropDownExpanded: () -> Boolean,
    onIsDropDownExpandedChange: (Boolean) -> Unit,
    buttonHeight: Dp,
    screenPadding: Dp,
    onShowDatePickerChange: (Boolean) -> Unit
) {
    val isNameError = when(name() == "" ||
            name().toCharArray().count() >= 12) {
        true -> { true }
        false -> { false }
    }
    val isAmountError = when(amount().toFloatOrNull()) {
        null -> { true }
        else -> { false }
    }

    val currentCalendarDate = Calendar.getInstance().timeInMillis
    val isDateError = when(finishDate() > currentCalendarDate) {
        true -> {
            false
        }
        false -> {
            true
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    CustomDialog(
        modifier = Modifier
            .size(
                screenFractionDp(
                    fraction = 0.65f,
                    isWidth = false
                )
            ),
        title = stringResource(id = R.string.add_option_saving_plan_title),
        onConfirm = {
            if(!isNameError && !isAmountError && !isDateError) {
                onAddSavingPlan(
                    SavingPlan(
                        name = name(),
                        amount = amount().toFloat().absoluteValue,
                        recurringType = getRecurringType { recurringType() },
                        dateEnd = Calendar.getInstance().apply {
                            timeInMillis = finishDate()
                        }
                    )
                )

                onDialogClose()
            }
        },
        onCancel = {
            onDialogClose()
        },
        onDismiss = {
            onDialogClose()
        },
        buttonHeight = buttonHeight,
        screenPadding = screenPadding
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = screenPadding),
            value = name(),
            onValueChange = onNameChange,
            label = {
                Text(text = "Name")
            },
            isError = isNameError
        )
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = screenPadding),
            value = amount(),
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            label = {
                Text(text = "Amount")
            },
            isError = isAmountError
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = screenPadding),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = null
                )
            },
            interactionSource = interactionSource,
            label = {
                Text("Plan End Date")
            },
            value = formatDate(
                Calendar
                    .getInstance()
                    .apply { timeInMillis = finishDate() }
            ),
            onValueChange = { },
            isError = isDateError,
            readOnly = true
        )

        RecurringTypeDropDown(
            recurringType = { recurringType() },
            onRecurringTypeChange = onRecurringTypeChange,
            isDropDownExpanded = { isDropDownExpanded() },
            onIsDropDownExpandedChange = onIsDropDownExpandedChange,
            label = "Save Every"
        )
    }

    if(interactionSource.collectIsPressedAsState().value) {
        onShowDatePickerChange(true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    onFinishDateChange: (Long) -> Unit,
    onShowDatePickerChange: (Boolean) -> Unit
) {
    val currentDate = Calendar.getInstance()
    val initialDateMillis = currentDate.timeInMillis
    val currentYear = currentDate.get(Calendar.YEAR)

    val maxYear = currentYear + 10

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        yearRange = IntRange(currentYear - 1, maxYear)
    )

    DatePickerDialog(
        onDismissRequest = {
            onShowDatePickerChange(false)
        },
        confirmButton = {
            IconButton(
                onClick = {
                    onShowDatePickerChange(false)
                    datePickerState.selectedDateMillis?.let { onFinishDateChange(it) }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_confirm),
                    contentDescription = null
                )
            }
        },
        dismissButton = {
            IconButton(
                onClick = {
                    onShowDatePickerChange(false)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = null
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun AddTransactionScreen(
    addTransactionState: () -> AddTransactionState,
    onEvent: (AddTransactionEvent) -> Unit
) {
    AddTransactionDialog(
        onAddLastTransaction = { transaction ->
            onEvent(AddTransactionEvent.OnAddTransaction(transaction))
        },
        onDialogClose = {
            onEvent(AddTransactionEvent.OnTransactionDialogClose)
        },
        name = { addTransactionState().addedItemName },
        onNameChange = { newName ->
            onEvent(AddTransactionEvent.OnAddedItemNameChange(newName))
        },
        amount = { addTransactionState().addedItemAmount },
        onAmountChange = { newAmount ->
            onEvent(AddTransactionEvent.OnAddedItemAmountChange(newAmount))
        },
        isDebit = { addTransactionState().addedItemIsDebit },
        onIsDebitChange = { isDebit ->
            onEvent(AddTransactionEvent.OnAddedItemIsDebitChange(isDebit))
        },
        buttonHeight = PaddingSizes.DialogButtonHeight,
        screenPadding = PaddingSizes.Screen,
        date = { addTransactionState().addedItemDate },
        onShowDatePickerChange = { showDatePicker ->
            onEvent(AddTransactionEvent.OnShowDatePickerChange(showDatePicker))
        }
    )

    if(addTransactionState().showDatePicker) {
        CustomDatePicker(
            onFinishDateChange = { newDate ->
                onEvent(AddTransactionEvent.OnAddedItemDate(newDate))
            },
            onShowDatePickerChange = { showDatePickerChange ->
                onEvent(AddTransactionEvent.OnShowDatePickerChange(showDatePickerChange))
            }
        )
    }
}

@Composable
fun AddSavingPlanScreen(
    addSavingPlanState: () -> AddSavingPlanState,
    onEvent: (AddSavingPlanEvent) -> Unit
) {
    AddSavingPlanDialog(
        onAddSavingPlan = { savingPlan ->
            onEvent(AddSavingPlanEvent.OnAddSavingPlan(savingPlan))
        },
        onDialogClose = {
            onEvent(AddSavingPlanEvent.OnSavingPlanDialogClose)
        },
        name = { addSavingPlanState().addedItemName },
        onNameChange = { newName ->
            onEvent(AddSavingPlanEvent.OnAddedItemNameChange(newName))
        },
        amount = { addSavingPlanState().addedItemAmount },
        onAmountChange = { newAmount ->
            onEvent(AddSavingPlanEvent.OnAddedItemAmountChange(newAmount))
        },
        recurringType = { addSavingPlanState().addedItemRecurringType },
        onRecurringTypeChange = { newRecurringType ->
            onEvent(AddSavingPlanEvent.OnAddedItemRecurringType(newRecurringType))
        },
        finishDate = { addSavingPlanState().addedItemDate },
        isDropDownExpanded = { addSavingPlanState().isRecurringTypeDropDownExpanded },
        onIsDropDownExpandedChange = { isDropDownExpanded ->
            onEvent(AddSavingPlanEvent.OnIsRecurringTypeDropDownExpandedChange(isDropDownExpanded))
        },
        buttonHeight = PaddingSizes.DialogButtonHeight,
        screenPadding = PaddingSizes.Screen,
        onShowDatePickerChange = { showDatePicker ->
            onEvent(AddSavingPlanEvent.OnShowDatePickerChange(showDatePicker))
        }
    )

    if(addSavingPlanState().showDatePicker) {
        CustomDatePicker(
            onFinishDateChange = { newDate ->
                onEvent(AddSavingPlanEvent.OnAddedItemDate(newDate))
            },
            onShowDatePickerChange = { showDatePickerChange ->
                onEvent(AddSavingPlanEvent.OnShowDatePickerChange(showDatePickerChange))
            }
        )
    }
}

@Composable
fun AddRecurringTransactionScreen(
    addRecurringTransactionState: () -> AddRecurringTransactionState,
    onEvent: (AddRecurringTransactionEvent) -> Unit
) {
    AddRecurringTransactionDialog(
        onDialogClose = {
            onEvent(AddRecurringTransactionEvent.OnRecurringTransactionDialogClose)
        },
        name = { addRecurringTransactionState().addedItemName },
        onNameChange = { newName ->
            onEvent(AddRecurringTransactionEvent.OnAddedItemNameChange(newName))
        },
        amount = { addRecurringTransactionState().addedItemAmount },
        onAmountChange = { newAmount ->
            onEvent(AddRecurringTransactionEvent.OnAddedItemAmountChange(newAmount))
        },
        isDebit = { addRecurringTransactionState().addedItemIsDebit },
        onIsDebitChange = { isDebit ->
            onEvent(AddRecurringTransactionEvent.OnAddedItemIsDebitChange(isDebit))
        },
        recurringType = { addRecurringTransactionState().addedItemRecurringType },
        onRecurringTypeChange = { newRecurringType ->
            onEvent(AddRecurringTransactionEvent.OnAddedItemRecurringType(newRecurringType))
        },
        buttonHeight = PaddingSizes.DialogButtonHeight,
        screenPadding = PaddingSizes.Screen,
        isDropDownExpanded = { addRecurringTransactionState().isRecurringTypeDropDownExpanded },
        onIsDropDownExpandedChange = { isDropDownExpanded ->
            onEvent(AddRecurringTransactionEvent.OnIsRecurringTypeDropDownExpandedChange(isDropDownExpanded))
        },
        onAddRecurringTransaction = { recurringTransaction ->
            onEvent(AddRecurringTransactionEvent.OnAddRecurringTransaction(recurringTransaction))
        }
    )
}

@Composable
fun AddRecurringTransactionDialog(
    onAddRecurringTransaction: (RecurringTransaction) -> Unit,
    onDialogClose: () -> Unit,
    name: () -> String,
    onNameChange: (String) -> Unit,
    amount: () -> String,
    onAmountChange: (String) -> Unit,
    isDebit: () -> Boolean,
    onIsDebitChange: (Boolean) -> Unit,
    recurringType: () -> String,
    onRecurringTypeChange: (String) -> Unit,
    isDropDownExpanded: () -> Boolean,
    onIsDropDownExpandedChange: (Boolean) -> Unit,
    buttonHeight: Dp,
    screenPadding: Dp
) {
    val isNameError = when(name() == "" ||
            name().toCharArray().count() >= 12) {
        true -> { true }
        false -> { false }
    }
    val isAmountError = when(amount().toFloatOrNull()) {
        null -> { true }
        else -> { false }
    }

    CustomDialog(
        modifier = Modifier
            .size(
                screenFractionDp(
                    fraction = 0.6f,
                    isWidth = false
                )
            ),
        title = stringResource(id = R.string.add_option_recurring_transaction_title),
        onConfirm = {
            if(!isNameError && !isAmountError) {
                onAddRecurringTransaction(
                    RecurringTransaction(
                        name = name(),
                        amount = amount().toFloat().absoluteValue,
                        isDebit = isDebit(),
                        recurringType = getRecurringType { recurringType() }
                    )
                )
                onDialogClose()
            }
        },
        onCancel = {
            onDialogClose()
        },
        onDismiss = {
            onDialogClose()
        },
        buttonHeight = buttonHeight,
        screenPadding = screenPadding
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = screenPadding),
            value = name(),
            onValueChange = onNameChange,
            label = {
                Text(text = "Name")
            },
            isError = isNameError
        )
        OutlinedTextField(
            modifier = Modifier.padding(horizontal = screenPadding),
            value = amount(),
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            label = {
                Text(text = "Amount")
            },
            isError = isAmountError
        )
        RecurringTypeDropDown(
            recurringType = { recurringType() },
            onRecurringTypeChange = onRecurringTypeChange,
            isDropDownExpanded = { isDropDownExpanded() },
            onIsDropDownExpandedChange = onIsDropDownExpandedChange,
            label = "Recurring Type"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isDebit(),
                    onClick = {
                        onIsDebitChange(false)
                    }
                )

                Text(
                    text = "Debit",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            )  {
                RadioButton(
                    selected = !isDebit(),
                    onClick = {
                        onIsDebitChange(true)
                    }
                )

                Text(
                    text = "Credit",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringTypeDropDown(
    label: String,
    recurringType: () -> String,
    onRecurringTypeChange: (String) -> Unit,
    isDropDownExpanded: () -> Boolean,
    onIsDropDownExpandedChange: (Boolean) -> Unit
) {
    val recurringTypes = listOf(
        RecurringType.DAILY.toString(),
        RecurringType.WEEKLY.toString(),
        RecurringType.MONTHLY.toString(),
        RecurringType.YEARLY.toString()
    )

    val interactionSource = remember { MutableInteractionSource() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingSizes.Screen)
    ) {
        OutlinedTextField(
            value = recurringType(),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isDropDownExpanded()
                )
            },
            interactionSource = interactionSource
        )

        DropdownMenu(
            modifier = Modifier
                .width(maxWidth / 1.5f),
            expanded = isDropDownExpanded(),
            onDismissRequest = { onIsDropDownExpandedChange(false) }
        ) {
            recurringTypes.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onRecurringTypeChange(item)
                        onIsDropDownExpandedChange(false)
                    },
                    text = {
                        Text(text = item)
                    }
                )
            }
        }
    }

    if(interactionSource.collectIsPressedAsState().value) {
        onIsDropDownExpandedChange(true)
    }
}

private fun getRecurringType(recurringType: () -> String) : RecurringType {
    return when(recurringType()) {
        RecurringType.DAILY.toString() -> {
            RecurringType.DAILY
        }
        RecurringType.WEEKLY.toString() -> {
            RecurringType.WEEKLY
        }
        RecurringType.MONTHLY.toString() -> {
            RecurringType.MONTHLY
        }
        RecurringType.YEARLY.toString() -> {
            RecurringType.YEARLY
        }
        else -> {
            RecurringType.ONE_TIME
        }
    }
}