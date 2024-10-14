package com.commercial.bills.ui.util.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.commercial.bills.ui.util.function.colorBlend

@Composable
fun CustomStickyHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = colorBlend(
            color1 = MaterialTheme.colorScheme.onBackground,
            color2 = Color.Transparent,
            intensity = 0.3f
        )
    )
}