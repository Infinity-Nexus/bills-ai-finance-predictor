package com.commercial.bills.ui.screen.prediction.composable.chart

import android.util.Range
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import javax.annotation.concurrent.Immutable

@Composable
fun CustomChart(
    modifier: Modifier = Modifier,
    charts: List<ChartValues>,
    axisValues: AxisValues,
    onViewersValueChange: List<(Float) -> Unit>,
    onSelectedChartRangeIndexChange: (Int) -> Unit,
    selectedChartRangeIndex: () -> Int,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val textMeasurer = rememberTextMeasurer()
    val backgroundColor = MaterialTheme.colorScheme.background
    var chartRanges = listOf<Range<Float>>()

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            chartRanges = getChartRanges(chartValuesSize = axisValues.values.size)
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            val xOffset = offset.x
                            chartRanges.forEachIndexed { index, range ->
                                if (index != selectedChartRangeIndex()) {
                                    if (range.contains(xOffset)) {
                                        onSelectedChartRangeIndexChange(index)
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            val axisValuesHeight = mutableListOf<Float>()

            axisValues.values.onEach { value ->
                val valueSize = textMeasurer
                    .measure(text = value)
                    .size
                val valueHeight = valueSize.height.toFloat()
                axisValuesHeight.add(valueHeight)
            }

            val bottomPadding = screenPadding.toPx() * 2f +
                    axisValuesHeight.max()

            val maxChartValues = charts.map {
                it.values.max()
            }
            val chartHeight = size.height - bottomPadding
            var maxChartsValue = maxChartValues.max()
            maxChartsValue *= maxChartsValue * chartHeight /
                    maxChartsValue * -1f + chartHeight

            drawLines(
                charts = charts,
                bottomPadding =  bottomPadding
            )

            drawPoints(
                charts = charts,
                radius = 20f,
                bottomPadding =  bottomPadding,
                backgroundColor = backgroundColor
            )

            updateViewersValue(
                charts = charts,
                onViewersValueChange = onViewersValueChange,
                selectedChartRangeIndex = { selectedChartRangeIndex() }
            )

            drawAxis(
                textMeasurer = textMeasurer,
                axisValues = axisValues,
                bottomPadding = bottomPadding,
                maxChartValue = maxChartsValue,
                axisIndicatorHeight = 8f,
                axisIndicatorToAxisValuePadding = contentPadding.toPx(),
                chartRanges = chartRanges,
                selectedChartRangeIndex = selectedChartRangeIndex()
            )
        }
    }
}

fun DrawScope.getChartRanges(chartValuesSize: Int): List<Range<Float>> {
    val chartRanges = mutableListOf<Range<Float>>()
    val xOffsetSize = size.width / (chartValuesSize + 1).toFloat()
    val horizontalPadding = size.width / ((chartValuesSize + 1) * 2).toFloat()

    val chartValuesXOffsets = mutableListOf<Float>()
    var chartValueXOffset = 0f
    for (iteration in 0..chartValuesSize) {
        if(iteration == 0) {
            chartValueXOffset = horizontalPadding
            chartValuesXOffsets.add(chartValueXOffset)
            continue
        }

        chartValueXOffset += xOffsetSize
        chartValuesXOffsets.add(chartValueXOffset)
    }

    var lastXOffset = 0f
    chartValuesXOffsets.forEachIndexed { index, xOffset ->
        lastXOffset = if(index != 0) {
            chartRanges.add(
                Range(
                    lastXOffset,
                    xOffset
                )
            )

            xOffset
        } else {
            xOffset
        }
    }

    return chartRanges
}

fun updateViewersValue(
    charts: List<ChartValues>,
    onViewersValueChange: List<(Float) -> Unit>,
    selectedChartRangeIndex: () -> Int,
) {
    if(selectedChartRangeIndex() == -1) return

    charts.forEachIndexed { index, chartValues ->
        val heldChartValue = chartValues.values.elementAt(selectedChartRangeIndex())
        onViewersValueChange.elementAt(index)(heldChartValue)
    }
}

fun DrawScope.drawLines(
    charts: List<ChartValues>,
    bottomPadding: Float
) {
    val chartHeight = size.height - bottomPadding
    val maxChartValues = charts.map { chartValues ->
        chartValues.values.max()
    }
    val maxChartsValue = maxChartValues.max()
    val pointsOffset = mutableListOf<Offset>()
    var pointXOffset: Float
    var pointYOffset: Float

    charts.forEach { chartValues ->
        pointsOffset.clear()
        pointsOffset.add(
            Offset(
                x = 0f,
                y = chartHeight
            )
        )

        chartValues.values.forEachIndexed { index, value ->
            pointXOffset = size.width /
                    (chartValues.values.size + 1).toFloat()
            pointYOffset = value * chartHeight /
                    maxChartsValue * -1f + chartHeight

            pointXOffset += pointsOffset.elementAt(index = index).x

            pointsOffset.add(
                Offset(
                    x = pointXOffset,
                    y = pointYOffset
                )
            )
        }

        pointsOffset.add(
            Offset(
                x = size.width,
                y = chartHeight
            )
        )

        pointsOffset.forEachIndexed { index, pointOffset ->
            if(index == pointsOffset.size - 1) return@forEach

            val nextPointOffset =  pointsOffset.elementAt(index = index + 1)

            drawLine(
                brush = chartValues.lineBrush,
                start = pointOffset,
                end = nextPointOffset,
                strokeWidth = 4f
            )
        }
    }
}

fun DrawScope.drawPoints(
    charts: List<ChartValues>,
    radius: Float,
    bottomPadding: Float,
    backgroundColor: Color
) {
    val chartHeight = size.height - bottomPadding
    val maxChartValues = charts.map { chartValues ->
        chartValues.values.max()
    }
    val maxChartsValue = maxChartValues.max()
    val pointXOffsets = mutableListOf<Float>()
    var pointXOffset: Float
    var pointYOffset: Float

    charts.forEach { chartValues ->
        chartValues.values.forEachIndexed { index, value ->
            pointXOffset = size.width /
                    (chartValues.values.size + 1).toFloat()
            pointYOffset = value * chartHeight /
                    maxChartsValue * -1f + chartHeight

            if(index > 0) {
                pointXOffset += pointXOffsets.elementAt(index = index - 1)
            }

            pointXOffsets.add(pointXOffset)

            drawCircle(
                color = chartValues.pointColor,
                radius = radius,
                center = Offset(
                    x = pointXOffset,
                    y = pointYOffset
                )
            )

            drawCircle(
                color = backgroundColor,
                radius = radius / 1.5f,
                center = Offset(
                    x = pointXOffset,
                    y = pointYOffset
                )
            )
        }
    }
}

fun DrawScope.drawAxis(
    textMeasurer: TextMeasurer,
    axisValues: AxisValues,
    bottomPadding: Float,
    maxChartValue: Float,
    axisIndicatorHeight: Float,
    axisIndicatorToAxisValuePadding: Float,
    chartRanges: List<Range<Float>>,
    selectedChartRangeIndex: Int,
) {
    val chartHeight = size.height - bottomPadding
    val axisValuesWidth = mutableListOf<Float>()

    axisValues.values.forEach { value ->
        val valueSize = textMeasurer
            .measure(text = value)
            .size
        val valueWidth = valueSize.width.toFloat()
        axisValuesWidth.add(valueWidth)
    }

    drawLine(
        color = axisValues.textStyle.color,
        start = Offset(
            x = 0f,
            y = chartHeight
        ),
        end = Offset(
            x = size.width,
            y = chartHeight
        )
    )

    val axisValuesXOffset = mutableListOf<Float>()
    var axisValueXOffset: Float

    axisValues.values.forEachIndexed { index, value ->
        axisValueXOffset = size.width /
                (axisValues.values.size + 1).toFloat()

        if (index > 0) {
            axisValueXOffset += axisValuesXOffset.elementAt(index = index - 1)
        }

        axisValuesXOffset.add(axisValueXOffset)

        val textXOffset = axisValueXOffset -
                axisValuesWidth.elementAt(index) / 2f
        val textYOffset = chartHeight +
                axisIndicatorHeight +
                axisIndicatorToAxisValuePadding

        drawText(
            textMeasurer = textMeasurer,
            text = value,
            style = axisValues.textStyle,
            topLeft = Offset(
                x = textXOffset,
                y = textYOffset
            )
        )
    }

    var axisLineXOffset: Float
    for (iteration in 0..axisValues.values.size) {
        axisLineXOffset = size.width /
                (axisValues.values.size + 1).toFloat() / 2f

        if (iteration > 0) {
            axisLineXOffset += axisValuesXOffset.elementAt(index = iteration - 1)
        }

        drawLine(
            color = axisValues.textStyle.color,
            start = Offset(
                x = axisLineXOffset,
                y = chartHeight
            ),
            end = Offset(
                x = axisLineXOffset,
                y = chartHeight + axisIndicatorHeight
            )
        )

        drawLine(
            color = axisValues.textStyle.color,
            start = Offset(
                x = axisLineXOffset,
                y = chartHeight
            ),
            end = Offset(
                x = axisLineXOffset,
                y = maxChartValue
            ),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(10f, 20f),
                phase = 25f
            ),
            alpha = 0.5f,
        )
    }

    if(selectedChartRangeIndex != -1) {
        val selectedChartRange = chartRanges.elementAt(selectedChartRangeIndex)

        drawLine(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    axisValues.textStyle.color
                )
            ),
            start = Offset(
                x = selectedChartRange.lower,
                y = chartHeight
            ),
            end = Offset(
                x = selectedChartRange.lower,
                y = maxChartValue
            ),
            strokeWidth = 8f
        )

        drawLine(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    axisValues.textStyle.color
                )
            ),
            start = Offset(
                x = selectedChartRange.upper,
                y = chartHeight
            ),
            end = Offset(
                x = selectedChartRange.upper,
                y = maxChartValue
            ),
            strokeWidth = 8f
        )
    }
}

@Immutable
data class AxisValues(
    val values: List<String>,
    val textStyle: TextStyle
)

@Immutable
data class ChartValues(
    val values: List<Float>,
    val lineBrush: Brush,
    val pointColor: Color
)