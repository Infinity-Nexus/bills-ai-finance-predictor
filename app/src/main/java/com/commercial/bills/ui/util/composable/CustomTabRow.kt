package com.commercial.bills.ui.util.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTabRow(
    items: () -> List<Int>,
    contentPadding: Dp,
    screenPadding: Dp,
    tabs: @Composable (() -> Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val tabInteractionSource = remember { MutableInteractionSource() }
    val pagerState = rememberPagerState { items().count() }

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabIndices ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabIndices[pagerState.currentPage])
                        .padding(horizontal = screenPadding)
                        .height(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.primary)
                )
            }
        ) {
            items().forEachIndexed { selectedTabIndex, title ->
                Text(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = tabInteractionSource
                        ) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(selectedTabIndex)
                            }
                        }
                        .padding(contentPadding),
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            key = {
                it
            }
        ) { selectedPageIndex ->
            tabs { selectedPageIndex }
        }
    }
}