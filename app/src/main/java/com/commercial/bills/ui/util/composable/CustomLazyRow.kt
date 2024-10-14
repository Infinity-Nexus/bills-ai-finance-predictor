package com.commercial.bills.ui.util.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun <T> CustomLazyRow(
    modifier: Modifier = Modifier,
    headline: @Composable ColumnScope.() -> Unit,
    items: () -> List<T>,
    screenPadding: Dp,
    contentPadding: Dp,
    itemComposables: LazyListScope.(() -> List<T>, Dp, Dp) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        headline()
        Box(modifier = Modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = listState
            ) {
                itemComposables(
                    { items() },
                    screenPadding,
                    contentPadding
                )
            }
            ToTopOfListButton(
                modifier = Modifier
                    .padding(end = contentPadding)
                    .align(Alignment.CenterEnd),
                firstVisibleItemIndex = 3,
                iconRotation = 90f,
                listState = listState,
                coroutineScope = coroutineScope
            )
        }
    }
}




