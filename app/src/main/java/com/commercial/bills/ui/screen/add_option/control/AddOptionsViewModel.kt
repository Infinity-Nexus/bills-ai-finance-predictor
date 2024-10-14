package com.commercial.bills.ui.screen.add_option.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.data.repository.BalanceRepository
import com.commercial.bills.data.repository.LastTransactionRepository
import com.commercial.bills.data.repository.RecurringTransactionRepository
import com.commercial.bills.data.repository.SavingPlanRepository
import com.commercial.bills.data.util.RecurringType
import com.commercial.bills.ui.screen.add_option.util.AddOptionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddOptionsViewModel @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val lastTransactionRepository: LastTransactionRepository,
    private val savingPlanRepository: SavingPlanRepository,
    private val recurringTransactionRepository: RecurringTransactionRepository
) : ViewModel() {
    private val _state = mutableStateOf(AddOptionsState())
    val state: State<AddOptionsState> = _state

    private val _addTransactionState = mutableStateOf(AddTransactionState())
    val addTransactionState: State<AddTransactionState> = _addTransactionState

    private val _addSavingPlanState = mutableStateOf(AddSavingPlanState())
    val addSavingPlanState: State<AddSavingPlanState> = _addSavingPlanState

    private val _addRecurringTransactionState = mutableStateOf(AddRecurringTransactionState())
    val addRecurringTransactionState: State<AddRecurringTransactionState> = _addRecurringTransactionState

    fun addOptionsEvent(addOptionsEvent: AddOptionsEvent) {
        viewModelScope.launch {
            when(addOptionsEvent) {
                is AddOptionsEvent.OnClickNumberChange -> {
                    _state.value = state.value.copy(
                        clickNumber = addOptionsEvent.newClickNumber
                    )
                }
                is AddOptionsEvent.OnAdStatusChange -> {
                    _state.value = state.value.copy(
                        adStatus = addOptionsEvent.adStatus
                    )
                }
                is AddOptionsEvent.OnShowBottomSheetChange -> {
                    _state.value = state.value.copy(
                        showBottomSheet = addOptionsEvent.showBottomSheet
                    )
                }
                is AddOptionsEvent.OnAddOptionsSelected -> {
                    _state.value = state.value.copy(
                        addOptionsSelected = addOptionsEvent.addOptionsSelected
                    )
                }
                is AddOptionsEvent.OnSelectedAddOptionChange -> {
                    val newClickNumber = when(addOptionsEvent.newAddOptionType) {
                        AddOptionType.NONE -> {
                            state.value.clickNumber
                        }
                        else -> {
                            state.value.clickNumber + 1
                        }
                    }
                    _state.value = state.value.copy(
                        selectedAddOption = addOptionsEvent.newAddOptionType,
                        clickNumber = newClickNumber,
                        showBottomSheet = false
                    )
                }
            }
        }
    }

    fun addTransactionEvent(addTransactionEvent: AddTransactionEvent) {
        viewModelScope.launch {
            when(addTransactionEvent) {
                is AddTransactionEvent.OnAddTransaction -> {
                    addLastTransaction(addTransactionEvent.transaction)
                }
                is AddTransactionEvent.OnAddedItemAmountChange -> {
                    _addTransactionState.value = addTransactionState.value.copy(
                        addedItemAmount = addTransactionEvent.newAmount
                    )
                }
                is AddTransactionEvent.OnAddedItemIsDebitChange -> {
                    _addTransactionState.value = addTransactionState.value.copy(
                        addedItemIsDebit = !addTransactionEvent.isDebit
                    )
                }
                is AddTransactionEvent.OnAddedItemNameChange -> {
                    _addTransactionState.value = addTransactionState.value.copy(
                        addedItemName = addTransactionEvent.newName
                    )
                }
                is AddTransactionEvent.OnTransactionDialogClose -> {
                    _addTransactionState.value = addTransactionState.value.copy(
                        addedItemName = "",
                        addedItemAmount = "",
                        addedItemIsDebit = true
                    )
                    addOptionsEvent(AddOptionsEvent.OnSelectedAddOptionChange(AddOptionType.NONE))
                    addOptionsEvent(AddOptionsEvent.OnShowBottomSheetChange(true))
                    addOptionsEvent(AddOptionsEvent.OnAddOptionsSelected(false))
                }
                is AddTransactionEvent.OnAddedItemDate -> {
                    _addTransactionState.value = addTransactionState.value.copy(
                        addedItemDate = addTransactionEvent.newDate
                    )
                }
                is AddTransactionEvent.OnShowDatePickerChange -> {
                    _addTransactionState.value = addTransactionState.value.copy(
                        showDatePicker = addTransactionEvent.showDatePicker
                    )
                }
            }
        }
    }

    fun addRecurringTransactionEvent(addRecurringTransactionEvent: AddRecurringTransactionEvent) {
        when(addRecurringTransactionEvent) {
            is AddRecurringTransactionEvent.OnAddRecurringTransaction -> {
                addRecurringTransaction(addRecurringTransactionEvent.recurringTransaction)
            }
            is AddRecurringTransactionEvent.OnAddedItemAmountChange -> {
                _addRecurringTransactionState.value = addRecurringTransactionState.value.copy(
                    addedItemAmount = addRecurringTransactionEvent.newAmount
                )
            }
            is AddRecurringTransactionEvent.OnAddedItemIsDebitChange -> {
                _addRecurringTransactionState.value = addRecurringTransactionState.value.copy(
                    addedItemIsDebit = !addRecurringTransactionEvent.isDebit
                )
            }
            is AddRecurringTransactionEvent.OnAddedItemNameChange -> {
                _addRecurringTransactionState.value = addRecurringTransactionState.value.copy(
                    addedItemName = addRecurringTransactionEvent.newName
                )
            }
            is AddRecurringTransactionEvent.OnAddedItemRecurringType -> {
                _addRecurringTransactionState.value = addRecurringTransactionState.value.copy(
                    addedItemRecurringType = addRecurringTransactionEvent.newRecurringType
                )
            }
            is AddRecurringTransactionEvent.OnIsRecurringTypeDropDownExpandedChange -> {
                _addRecurringTransactionState.value = addRecurringTransactionState.value.copy(
                    isRecurringTypeDropDownExpanded = addRecurringTransactionEvent.isDropDownExpanded
                )
            }
            AddRecurringTransactionEvent.OnRecurringTransactionDialogClose -> {
                _addRecurringTransactionState.value = addRecurringTransactionState.value.copy(
                    addedItemName = "",
                    addedItemAmount = "",
                    addedItemIsDebit = true,
                    addedItemRecurringType = RecurringType.MONTHLY.toString(),
                    isRecurringTypeDropDownExpanded = false
                )
                addOptionsEvent(AddOptionsEvent.OnSelectedAddOptionChange(AddOptionType.NONE))
                addOptionsEvent(AddOptionsEvent.OnShowBottomSheetChange(true))
                addOptionsEvent(AddOptionsEvent.OnAddOptionsSelected(false))
            }
        }
    }

    fun addSavingPlanEvent(addSavingPlanEvent: AddSavingPlanEvent) {
        when(addSavingPlanEvent) {
            is AddSavingPlanEvent.OnAddSavingPlan -> {
                addSavingPlan(addSavingPlanEvent.savingPlan)
            }
            is AddSavingPlanEvent.OnAddedItemAmountChange -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    addedItemAmount = addSavingPlanEvent.newAmount
                )
            }
            is AddSavingPlanEvent.OnAddedItemDate -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    addedItemDate = addSavingPlanEvent.newDate
                )
            }
            is AddSavingPlanEvent.OnAddedItemNameChange -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    addedItemName = addSavingPlanEvent.newName
                )
            }
            is AddSavingPlanEvent.OnAddedItemRecurringType -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    addedItemRecurringType = addSavingPlanEvent.newRecurringType
                )
            }
            is AddSavingPlanEvent.OnIsRecurringTypeDropDownExpandedChange -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    isRecurringTypeDropDownExpanded = addSavingPlanEvent.isDropDownExpanded
                )
            }
            AddSavingPlanEvent.OnSavingPlanDialogClose -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    addedItemName = "",
                    addedItemAmount = "",
                    addedItemRecurringType = RecurringType.MONTHLY.toString(),
                    isRecurringTypeDropDownExpanded = false,
                    addedItemDate = Calendar.getInstance().timeInMillis,
                    showDatePicker = false
                )
                addOptionsEvent(AddOptionsEvent.OnSelectedAddOptionChange(AddOptionType.NONE))
                addOptionsEvent(AddOptionsEvent.OnShowBottomSheetChange(true))
                addOptionsEvent(AddOptionsEvent.OnAddOptionsSelected(false))
            }
            is AddSavingPlanEvent.OnShowDatePickerChange -> {
                _addSavingPlanState.value = addSavingPlanState.value.copy(
                    showDatePicker = addSavingPlanEvent.showDatePicker
                )
            }
        }
    }

    private fun addRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            recurringTransactionRepository.insertRecurringTransaction(recurringTransaction)
        }
    }

    private fun addSavingPlan(savingPlan: SavingPlan) {
        viewModelScope.launch(Dispatchers.IO) {
            savingPlanRepository.insertSavingPlan(savingPlan)
        }
    }

    private fun addLastTransaction(lastTransaction: LastTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            lastTransactionRepository.insertLastTransaction(lastTransaction)
            when(lastTransaction.isDebit) {
                true -> {
                    balanceRepository.decreaseBalance(lastTransaction.amount)
                }
                false -> {
                    balanceRepository.increaseBalance(lastTransaction.amount)
                }
            }
        }
    }
}