package com.commercial.bills.ui.util.function

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun colorBlend(
    color1 : Color,
    color2 : Color,
    @FloatRange(from = 0.0, to = 1.0) intensity : Float
) : Color {
    return Color(
        ColorUtils.blendARGB(
            color1.toArgb(),
            color2.toArgb(),
            intensity
        )
    )
}