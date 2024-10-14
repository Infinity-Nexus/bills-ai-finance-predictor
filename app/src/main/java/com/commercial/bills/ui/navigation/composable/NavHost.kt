package com.commercial.bills.ui.navigation.composable

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.commercial.bills.ui.navigation.util.NavBarRoutes
import com.commercial.bills.ui.screen.budget.composable.BudgetScreen
import com.commercial.bills.ui.screen.budget.control.BudgetViewModel
import com.commercial.bills.ui.screen.home.composable.HomeScreen
import com.commercial.bills.ui.screen.home.control.HomeViewModel
import com.commercial.bills.ui.screen.prediction.composable.PredictionScreen
import com.commercial.bills.ui.screen.prediction.control.PredictionViewModel
import com.commercial.bills.ui.screen.search.composable.SearchScreen
import com.commercial.bills.ui.screen.search.control.SearchViewModel
import androidx.navigation.compose.NavHost
import com.commercial.bills.MainActivity
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavHost(
    context: Context,
    navHostController: () -> NavHostController,
    homeViewModel: HomeViewModel,
    budgetViewModel: BudgetViewModel,
    predictionViewModel: PredictionViewModel,
    searchViewModel: SearchViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    activity: MainActivity
) {
    val homeRoute = context.resources.getString(NavBarRoutes.Home.route)
    val balanceRoute = context.resources.getString(NavBarRoutes.Budget.route)
    val predictionRoute = context.resources.getString(NavBarRoutes.Prediction.route)
    val searchRoute = context.resources.getString(NavBarRoutes.Search.route)

    NavHost(
        navController = navHostController(),
        startDestination = homeRoute
    ) {
        composable(route = homeRoute) {
            HomeScreen(
                homeState = { homeViewModel.state.value },
                onEvent = homeViewModel::onEvent,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
        composable(route = balanceRoute) {
            BudgetScreen(
                budgetState = { budgetViewModel.state.value },
                onEvent = budgetViewModel::onEvent,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
        composable(route = predictionRoute) {
            PredictionScreen(
                predictionState = { predictionViewModel.state.value },
                onEvent = predictionViewModel::onEvent,
                activity = activity
            )
        }
        composable(route = searchRoute) {
            SearchScreen(
                searchState = { searchViewModel.state.value },
                onEvent = searchViewModel::onEvent,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
    }
}
