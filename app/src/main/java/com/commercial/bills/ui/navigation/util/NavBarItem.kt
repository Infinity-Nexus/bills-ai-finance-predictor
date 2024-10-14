package com.commercial.bills.ui.navigation.util

import javax.annotation.concurrent.Immutable

@Immutable
data class NavBarItem(
    val title: Int,
    val icon: Int,
    val route: Int
)