package com.commercial.bills.ui.util.composable

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun screenFractionDp(
    @FloatRange(from = 0.0, to = 1.0) fraction: Float,
    isWidth: Boolean = true
) : Dp {
    val configuration = LocalContext.current.resources.configuration
    val screenWidthDp = configuration.screenWidthDp
    val screeHeightDp = configuration.screenHeightDp
    val newWidth = screenWidthDp * fraction
    val newHeight = screeHeightDp * fraction

    return when(isWidth) {
        true -> {
            if(fraction != 0f) newWidth.dp
            else screenWidthDp.dp
        }
        false -> {
            if(fraction != 0f) newHeight.dp
            else screeHeightDp.dp
        }
    }
}