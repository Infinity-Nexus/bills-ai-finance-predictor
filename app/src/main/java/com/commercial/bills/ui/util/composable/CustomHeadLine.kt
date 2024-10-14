package com.commercial.bills.ui.util.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomHeadLine(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Start,
        style = textStyle
    )
}