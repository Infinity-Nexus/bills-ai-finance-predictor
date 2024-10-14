package com.commercial.bills.ui.util.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.commercial.bills.ui.util.function.colorBlend
import com.commercial.bills.ui.util.function.formatCurrency

@Composable
fun CustomLinearProgressIndicator(
    goal: () -> Float,
    progress: () -> Float,
) {
    val goalColor = when(progress() == goal()) {
        true -> ProgressIndicatorDefaults.linearColor
        false -> colorBlend(
            color1 = ProgressIndicatorDefaults.linearColor,
            color2 = Color.Transparent,
            intensity = 0.5f
        )
    }

    LinearProgressIndicator(
        progress = progress() / goal(),
        trackColor = MaterialTheme.colorScheme.background,
        strokeCap = StrokeCap.Round
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatCurrency(progress()),
            style = MaterialTheme.typography.titleSmall,
            color = ProgressIndicatorDefaults.linearColor,
            maxLines = 1,
            softWrap = true
        )
        Text(
            text = formatCurrency(goal()),
            style = MaterialTheme.typography.titleSmall,
            color = goalColor,
            maxLines = 1,
            softWrap = true
        )
    }
}