package com.commercial.bills

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.data.repository.BalanceRepository
import com.commercial.bills.data.repository.LastTransactionRepository
import com.commercial.bills.data.repository.RecurringTransactionRepository
import com.commercial.bills.data.repository.SavingPlanRepository
import com.commercial.bills.data.util.RecurringType
import com.commercial.bills.ui.screen.add_option.control.AddOptionsViewModel
import com.commercial.bills.ui.screen.budget.control.BudgetViewModel
import com.commercial.bills.ui.screen.home.control.HomeViewModel
import com.commercial.bills.ui.screen.main.composable.MainScreen
import com.commercial.bills.ui.screen.prediction.control.PredictionViewModel
import com.commercial.bills.ui.screen.search.control.SearchViewModel
import com.commercial.bills.ui.theme.BillsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var predictionViewModel: PredictionViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var addOptionsViewModel: AddOptionsViewModel

    @Inject lateinit var balanceRepository: BalanceRepository
    @Inject lateinit var lastTransactionRepository: LastTransactionRepository
    @Inject lateinit var recurringTransactionRepository: RecurringTransactionRepository
    @Inject lateinit var savingPlanRepository: SavingPlanRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(owner = this)[HomeViewModel::class.java]
        budgetViewModel = ViewModelProvider(owner = this)[BudgetViewModel::class.java]
        predictionViewModel = ViewModelProvider(owner = this)[PredictionViewModel::class.java]
        searchViewModel = ViewModelProvider(owner = this)[SearchViewModel::class.java]
        addOptionsViewModel = ViewModelProvider(owner = this)[AddOptionsViewModel::class.java]

        setContent {
            BillsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        homeViewModel = homeViewModel,
                        budgetViewModel = budgetViewModel,
                        predictionViewModel = predictionViewModel,
                        searchViewModel = searchViewModel,
                        addOptionsViewModel = addOptionsViewModel,
                        activity = this
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            invokeOverdueRecurringTransactions()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            invokeOverdueSavingPlans()
        }
    }

    private suspend fun invokeOverdueSavingPlans() {
        savingPlanRepository.getSavingPlansNoFlow().forEach { savingPlan ->
            val currentDate = Calendar.getInstance()
            val nextTimeToInvoke = savingPlan.nextTimeToInvoke

            when(savingPlan.recurringType) {
                RecurringType.DAILY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {

                        increaseSavingPlan(
                            savingPlan,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.DAY_OF_YEAR, 1)

                        savingPlanRepository.setSavingPlanNextTimeToInvoke(
                            id = savingPlan.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                RecurringType.WEEKLY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        increaseSavingPlan(
                            savingPlan,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.WEEK_OF_YEAR, 1)

                        savingPlanRepository.setSavingPlanNextTimeToInvoke(
                            id = savingPlan.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                RecurringType.MONTHLY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        increaseSavingPlan(
                            savingPlan,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.MONTH, 1)

                        savingPlanRepository.setSavingPlanNextTimeToInvoke(
                            id = savingPlan.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                RecurringType.YEARLY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        increaseSavingPlan(
                            savingPlan,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.YEAR, 1)

                        savingPlanRepository.setSavingPlanNextTimeToInvoke(
                            id = savingPlan.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private suspend fun increaseSavingPlan(
        savingPlan: SavingPlan,
        nextTimeToInvoke: Calendar
    ) {
        if(savingPlan.savedAmount + savingPlan.amountToSave()
            >= savingPlan.amount
            || nextTimeToInvoke >= savingPlan.dateEnd) {
            lastTransactionRepository.insertLastTransaction(
                LastTransaction(
                    name = savingPlan.name,
                    amount = savingPlan.amount,
                    date = nextTimeToInvoke
                )
            )

            balanceRepository.decreaseBalance(
                savingPlan.amount
            )

            savingPlanRepository.deleteSavingPlan(savingPlan)
        }
        else {
            savingPlanRepository.increaseSavedAmount(
                savingPlan.amountToSave(),
                savingPlan.id
            )
        }
    }

    private suspend fun invokeOverdueRecurringTransactions() {
        recurringTransactionRepository.getRecurringTransactionsNoFlow().forEach { recurringTransaction ->
            val currentDate = Calendar.getInstance()
            val nextTimeToInvoke = recurringTransaction.nextTimeToInvoke

            when(recurringTransaction.recurringType) {
                RecurringType.DAILY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        invokeRecurringTransaction(
                            recurringTransaction,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.DAY_OF_YEAR, 1)

                        recurringTransactionRepository.setRecurringTransactionNextTimeToInvoke(
                            id = recurringTransaction.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                RecurringType.WEEKLY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        invokeRecurringTransaction(
                            recurringTransaction,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.WEEK_OF_YEAR, 1)

                        recurringTransactionRepository.setRecurringTransactionNextTimeToInvoke(
                            id = recurringTransaction.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                RecurringType.MONTHLY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        invokeRecurringTransaction(
                            recurringTransaction,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.MONTH, 1)

                        recurringTransactionRepository.setRecurringTransactionNextTimeToInvoke(
                            id = recurringTransaction.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                RecurringType.YEARLY -> {
                    while(currentDate.timeInMillis >=
                        nextTimeToInvoke.timeInMillis) {
                        invokeRecurringTransaction(
                            recurringTransaction,
                            nextTimeToInvoke
                        )

                        nextTimeToInvoke.add(Calendar.YEAR, 1)

                        recurringTransactionRepository.setRecurringTransactionNextTimeToInvoke(
                            id = recurringTransaction.id,
                            newDate = nextTimeToInvoke
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private suspend fun invokeRecurringTransaction (
        recurringTransaction: RecurringTransaction,
        nextTimeToInvoke: Calendar
    ) {
        lastTransactionRepository.insertLastTransaction(
            LastTransaction(
                name = recurringTransaction.name,
                amount = recurringTransaction.amount,
                isDebit = recurringTransaction.isDebit,
                date = nextTimeToInvoke
            )
        )

        when(recurringTransaction.isDebit) {
            false -> {
                balanceRepository.increaseBalance(recurringTransaction.amount)
            }
            true -> {
                balanceRepository.decreaseBalance(recurringTransaction.amount)
            }
        }
    }
}







