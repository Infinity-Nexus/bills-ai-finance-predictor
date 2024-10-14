package com.commercial.bills.ui.navigation.composable

import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.commercial.bills.MainActivity
import com.commercial.bills.ui.navigation.function.onClickNavBarItem
import com.commercial.bills.ui.navigation.util.NavBarItems
import com.commercial.bills.ui.navigation.util.NavBarRoutes
import com.commercial.bills.ui.screen.add_option.composable.AddOptions
import com.commercial.bills.ui.screen.add_option.control.AddOptionsEvent
import com.commercial.bills.ui.screen.add_option.control.AddOptionsViewModel
import com.commercial.bills.ui.util.composable.screenFractionDp
import com.commercial.bills.ui.util.function.colorBlend

@Composable
fun NavBar(
    context: Context,
    navHostController: () -> NavHostController,
    addOptionsViewModel: AddOptionsViewModel,
    activity: MainActivity
) {
    val navBarHeight = screenFractionDp(
        fraction = 0.07f,
        isWidth = false
    )
    val topOutlineColor = colorBlend(
        color1 = Color.White,
        color2 = Color.Transparent,
        intensity = 0.5f
    )

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .height(navBarHeight)
            .drawBehind {
                val strokeWidth = 1 * density

                drawLine(
                    topOutlineColor,
                    Offset(0f, 0f),
                    Offset(size.width, 0f),
                    strokeWidth
                )
            }
    ) {
        val backStackEntry by navHostController().currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.Items.forEach { navBarItem ->
            val navBarRoute = context.resources.getString(navBarItem.route)
            val navBarTitle = context.resources.getString(navBarItem.title)

            NavigationBarItem(
                selected = currentRoute == navBarRoute,
                onClick = {
                    when(navBarItem.route) {
                        NavBarRoutes.Add.route -> {
                            addOptionsViewModel
                                .addOptionsEvent(
                                    AddOptionsEvent.OnAddOptionsSelected(true)
                            )
                        }
                        else -> {
                            onClickNavBarItem(
                                navHostController = navHostController(),
                                navBarRoute = navBarRoute
                            )
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navBarItem.icon),
                        contentDescription = navBarTitle
                    )
                }
            )
        }
    }

    AddOptions(
        addOptionsViewModel = addOptionsViewModel,
        activity = activity
    )
}