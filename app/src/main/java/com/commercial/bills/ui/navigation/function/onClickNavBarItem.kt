package com.commercial.bills.ui.navigation.function

import androidx.navigation.NavHostController

fun onClickNavBarItem(
    navHostController: NavHostController,
    navBarRoute: String
) {
    navHostController.navigate(route = navBarRoute) {
        popUpTo(id = navHostController.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

