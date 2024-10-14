package com.commercial.bills.ui.screen.prediction.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.commercial.bills.MainActivity
import com.commercial.bills.R
import com.commercial.bills.ui.screen.prediction.composable.chart.AxisValues
import com.commercial.bills.ui.screen.prediction.composable.chart.ChartValues
import com.commercial.bills.ui.screen.prediction.composable.chart.CustomChart
import com.commercial.bills.ui.screen.prediction.composable.selection.PredictionSelectionItems
import com.commercial.bills.ui.screen.prediction.control.PredictionEvent
import com.commercial.bills.ui.screen.prediction.control.PredictionState
import com.commercial.bills.ui.screen.prediction.util.ForecastManager
import com.commercial.bills.ui.theme.PaddingSizes
import com.commercial.bills.ui.util.composable.screenFractionDp
import java.util.Calendar
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun PredictionScreen(
    predictionState: () -> PredictionState,
    onEvent: (PredictionEvent) -> Unit,
    activity: MainActivity
) {
    DisposableEffect(Unit) {
        onDispose {
            onEvent(PredictionEvent.OnShowPredictionScreenChange(false))
            onEvent(PredictionEvent.OnShowAdChange(false))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = PaddingSizes.Screen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PredictionSection(
            modifier = Modifier.padding(bottom = PaddingSizes.Screen)
        )

        PredictionSelectionSection(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    screenFractionDp(
                        fraction = 0.18f,
                        isWidth = false
                    )
                )
                .padding(
                    start = PaddingSizes.Screen,
                    end = PaddingSizes.Screen,
                    bottom = PaddingSizes.Screen
                ),
            incomeAmount = { predictionState().incomeAmount },
            expensesAmount = { predictionState().expensesAmount },
            contentPadding = PaddingSizes.Content
        )

        PredictionChartSection(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    screenFractionDp(
                        fraction = 0.4f,
                        isWidth = false
                    )
                ),
            onViewersValueChange = listOf(
                { newAmount ->
                    onEvent(PredictionEvent.OnIncomeAmountChange(newAmount))
                },
                { newAmount ->
                    onEvent(PredictionEvent.OnExpensesAmountChange(newAmount))
                }
            ),
            onSelectedChartRangeIndexChange = { newIndex ->
                onEvent(PredictionEvent.OnSelectedChartRangeIndexChange(newIndex))
            },
            incomePastValues = { predictionState().incomePastValues },
            expensesPastValues = { predictionState().expensesPastValues },
            screenPadding = PaddingSizes.Screen,
            selectedChartRangeIndex = { predictionState().selectedChartRangeIndex },
            contentPadding = PaddingSizes.Content
        )
    }
}

@Composable
fun PredictionSection(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.prediction_headline_name),
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun PredictionSelectionSection(
    modifier: Modifier = Modifier,
    incomeAmount: () -> Float,
    expensesAmount: () -> Float,
    contentPadding: Dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PredictionSelectionItems(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            incomeAmount = { incomeAmount() },
            expensesAmount = { expensesAmount() },
            contentPadding = contentPadding
        )
    }
}

@Composable
fun PredictionChartSection(
    modifier: Modifier = Modifier,
    selectedChartRangeIndex: () -> Int,
    onViewersValueChange: List<(Float) -> Unit>,
    onSelectedChartRangeIndexChange: (Int) -> Unit,
    incomePastValues: () -> List<Float>,
    expensesPastValues: () -> List<Float>,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val futureDates = getFutureDates()
    val incomeForecasts = getForecasts(
        pastValues = { incomePastValues() },
        steps = futureDates.size
    )
    val expensesForecasts = getForecasts(
        pastValues = { expensesPastValues() },
        steps = futureDates.size
    )

    CustomChart(
        modifier = modifier,
        charts = listOf(
            ChartValues(
                values = incomeForecasts,
                lineBrush = Brush.verticalGradient(
                    listOf(
                        Color.Green,
                        Color.Transparent
                    )
                ),
                pointColor = Color.Green
            ),
            ChartValues(
                values = expensesForecasts,
                lineBrush = Brush.verticalGradient(
                    listOf(
                        Color.Red,
                        Color.Transparent
                    )
                ),
                pointColor = Color.Red
            )
        ),
        axisValues = AxisValues(
            values = futureDates,
            textStyle = TextStyle(
                fontSize = MaterialTheme
                    .typography
                    .bodySmall
                    .fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
        ),
        onViewersValueChange = onViewersValueChange,
        onSelectedChartRangeIndexChange = onSelectedChartRangeIndexChange,
        selectedChartRangeIndex = { selectedChartRangeIndex() },
        screenPadding = screenPadding,
        contentPadding = contentPadding
    )
}

fun getFutureDates() : List<String> {
    val currentDate = Calendar.getInstance()

    val firstDate = currentDate.clone() as Calendar
    firstDate.add(Calendar.MONTH, 1)

    val secondDate = currentDate.clone() as Calendar
    secondDate.add(Calendar.MONTH, 2)

    val thirdDate = currentDate.clone() as Calendar
    thirdDate.add(Calendar.MONTH, 3)

    val fourthDate = currentDate.clone() as Calendar
    fourthDate.add(Calendar.MONTH, 4)

    return listOf(
        firstDate.getDisplayName(
            Calendar.MONTH,
            Calendar.SHORT,
            Locale.getDefault()
        ),
        secondDate.getDisplayName(
            Calendar.MONTH,
            Calendar.SHORT,
            Locale.getDefault()
        ),
        thirdDate.getDisplayName(
            Calendar.MONTH,
            Calendar.SHORT,
            Locale.getDefault()
        ),
        fourthDate.getDisplayName(
            Calendar.MONTH,
            Calendar.SHORT,
            Locale.getDefault()
        )
    ) as List<String>
}

@Composable
fun getForecasts(
    pastValues: () -> List<Float>,
    steps: Int
) : List<Float> {
    val forecastValues: List<Float> =
        ForecastManager.forecastValues(pastValues(), steps)

    return forecastValues
}