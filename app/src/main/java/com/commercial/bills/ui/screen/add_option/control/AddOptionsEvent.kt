package com.commercial.bills.ui.screen.add_option.control

import androidx.compose.runtime.Stable
import com.commercial.bills.ui.screen.add_option.util.AddOptionType

@Stable
sealed class AddOptionsEvent {
    data class OnAdStatusChange(val adStatus: Boolean) : AddOptionsEvent()
    data class OnClickNumberChange(val newClickNumber: Int) : AddOptionsEvent()
    data class OnAddOptionsSelected(val addOptionsSelected: Boolean) : AddOptionsEvent()
    data class OnShowBottomSheetChange(val showBottomSheet: Boolean) : AddOptionsEvent()
    data class OnSelectedAddOptionChange(val newAddOptionType: AddOptionType) : AddOptionsEvent()
}