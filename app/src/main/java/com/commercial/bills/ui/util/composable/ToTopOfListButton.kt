package com.commercial.bills.ui.util.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.commercial.bills.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ToTopOfListButton(
    modifier: Modifier = Modifier,
    firstVisibleItemIndex: Int,
    iconRotation: Float,
    listState: LazyListState,
    coroutineScope: CoroutineScope
) {
    val displayToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > firstVisibleItemIndex
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = displayToTopButton
    ) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }
            }
        ) {
            Icon(
                modifier = Modifier.graphicsLayer {
                    this.rotationZ = iconRotation
                },
                painter = painterResource(id = R.drawable.ic_to_top_of_list),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null
            )
        }
    }
}