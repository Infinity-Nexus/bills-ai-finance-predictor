package com.commercial.bills.ui.navigation.util

import androidx.compose.runtime.Stable
import com.commercial.bills.R

@Stable
sealed class NavBarRoutes (val route: Int) {
    data object Home: NavBarRoutes(route = R.string.nav_bar_item_home_name)
    data object Budget: NavBarRoutes(route = R.string.nav_bar_item_budget_name)
    data object Add: NavBarRoutes(route = R.string.nav_bar_item_add_name)
    data object Prediction: NavBarRoutes(route = R.string.nav_bar_item_prediction_name)
    data object Search: NavBarRoutes(route = R.string.nav_bar_item_search_name)
}