package com.commercial.bills.ui.screen.add_option.util

import javax.annotation.concurrent.Immutable

@Immutable
data class AddOption(
    val title: Int,
    val optionType: AddOptionType,
    val icon: Int
)
