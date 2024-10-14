package com.commercial.bills.ui.screen.add_option.control

import com.commercial.bills.ui.screen.add_option.util.AddOptionType
import javax.annotation.concurrent.Immutable

@Immutable
data class AddOptionsState(
    val addOptionsSelected: Boolean = false,
    val selectedAddOption: AddOptionType = AddOptionType.NONE,
    val showBottomSheet: Boolean = true,
    val clickNumber: Int = 0,
    val adStatus: Boolean = false
)