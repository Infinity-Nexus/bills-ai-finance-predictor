package com.commercial.bills.ui.screen.add_option.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commercial.bills.ui.screen.add_option.util.AddOptionType
import com.commercial.bills.ui.screen.add_option.util.AddOptions
import com.commercial.bills.ui.util.function.colorBlend
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionsBottomSheet(
    onShowBottomSheetChange: (Boolean) -> Unit,
    onAddOptionsSelectedChange: (Boolean) -> Unit,
    onSelectedAddOptionChange: (AddOptionType) -> Unit,
    screenPadding: Dp,
    contentPadding: Dp
) {
    val configuration = LocalContext.current.resources.configuration
    val screenHeightDp = configuration.screenHeightDp
    val bottomSheetHeight = screenHeightDp * 0.4f
    val sheetState = rememberModalBottomSheetState()
    val interactionSource = remember { MutableInteractionSource() }

    ModalBottomSheet(
        onDismissRequest = {
            onShowBottomSheetChange(true)
            onAddOptionsSelectedChange(false)
        },
        sheetState = sheetState
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomSheetHeight.dp)
                .padding(horizontal = screenPadding),
            columns = GridCells.Fixed(3)
        ) {
            items(AddOptions.options) { addOption ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onSelectedAddOptionChange(addOption.optionType)
                            }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        1.dp,
                                        colorBlend(
                                            color1 = Color.White,
                                            color2 = Color.Transparent,
                                            intensity = 0.5f
                                        ),
                                        CircleShape
                                    )
                            )

                            Icon(
                                painter = painterResource(id = addOption.icon),
                                contentDescription = stringResource(id = addOption.title)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(contentPadding / 2))

                    Text(
                        text = stringResource(id = addOption.title),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}