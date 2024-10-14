package com.commercial.bills.ui.screen.add_option.control

import androidx.compose.runtime.Stable
import com.commercial.bills.data.entity.SavingPlan

@Stable
sealed class AddSavingPlanEvent {
    data class OnShowDatePickerChange(val showDatePicker: Boolean) : AddSavingPlanEvent()
    data object OnSavingPlanDialogClose : AddSavingPlanEvent()
    data class OnAddedItemNameChange(val newName: String) : AddSavingPlanEvent()
    data class OnAddedItemAmountChange(val newAmount: String) : AddSavingPlanEvent()
    data class OnAddedItemRecurringType(val newRecurringType: String) : AddSavingPlanEvent()
    data class OnAddSavingPlan(val savingPlan: SavingPlan) : AddSavingPlanEvent()
    data class OnAddedItemDate(val newDate: Long) : AddSavingPlanEvent()
    data class OnIsRecurringTypeDropDownExpandedChange(val isDropDownExpanded: Boolean) : AddSavingPlanEvent()
}