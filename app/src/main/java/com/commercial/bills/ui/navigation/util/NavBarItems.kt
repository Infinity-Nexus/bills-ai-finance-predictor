package com.commercial.bills.ui.navigation.util

import androidx.compose.runtime.Stable
import com.commercial.bills.R

@Stable
object NavBarItems {
    val Items = listOf(
        NavBarItem(
            title = R.string.nav_bar_item_home_name,
            icon = NavBarIcons.HomeIcon,
            route = NavBarRoutes.Home.route
        ),
        NavBarItem(
            title = R.string.nav_bar_item_budget_name,
            icon = NavBarIcons.BudgetIcon,
            route = NavBarRoutes.Budget.route
        ),
        NavBarItem(
            title = R.string.nav_bar_item_add_name,
            icon = NavBarIcons.AddIcon,
            route = NavBarRoutes.Add.route
        ),
        NavBarItem(
            title = R.string.nav_bar_item_prediction_name,
            icon = NavBarIcons.PredictionIcon,
            route = NavBarRoutes.Prediction.route
        ),
        NavBarItem(
            title = R.string.nav_bar_item_search_name,
            icon = NavBarIcons.SearchIcon,
            route = NavBarRoutes.Search.route
        )
    )
}