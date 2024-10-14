package com.commercial.bills.ui.screen.main.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.commercial.bills.MainActivity
import com.commercial.bills.ui.navigation.composable.NavBar
import com.commercial.bills.ui.navigation.composable.NavHost
import com.commercial.bills.ui.screen.add_option.control.AddOptionsViewModel
import com.commercial.bills.ui.screen.budget.control.BudgetViewModel
import com.commercial.bills.ui.screen.home.control.HomeViewModel
import com.commercial.bills.ui.screen.prediction.control.PredictionViewModel
import com.commercial.bills.ui.screen.search.control.SearchViewModel
import com.commercial.bills.ui.theme.PaddingSizes

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    budgetViewModel: BudgetViewModel,
    predictionViewModel: PredictionViewModel,
    searchViewModel: SearchViewModel,
    addOptionsViewModel: AddOptionsViewModel,
    activity: MainActivity
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = PaddingSizes.Screen),
                hostState = snackbarHostState
            )
        },
        bottomBar = {
            NavBar(
                context = context,
                navHostController = { navController },
                addOptionsViewModel = addOptionsViewModel,
                activity = activity
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            NavHost(
                context = context,
                navHostController = { navController },
                homeViewModel = homeViewModel,
                budgetViewModel = budgetViewModel,
                predictionViewModel = predictionViewModel,
                searchViewModel = searchViewModel,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope,
                activity = activity
            )
        }
    }
}





