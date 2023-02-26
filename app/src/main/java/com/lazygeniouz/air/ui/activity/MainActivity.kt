package com.lazygeniouz.air.ui.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alorma.compose.settings.storage.base.getValue
import com.alorma.compose.settings.storage.preferences.rememberPreferenceBooleanSettingState
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.nav.AirNavHost
import com.lazygeniouz.air.nav.Screen
import com.lazygeniouz.air.ui.components.OnResumeEvent
import com.lazygeniouz.air.ui.components.RootNotAvailable
import com.lazygeniouz.air.ui.theme.ThemedSurface
import com.lazygeniouz.air.utils.misc.Constants
import com.lazygeniouz.air.utils.work.AdIdResetWorker

class MainActivity : ComponentActivity() {

    private val adIdViewModel: AdIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ThemedSurface { AdIdResetApp(adIdViewModel) } }
    }
}

@Composable
fun AdIdResetApp(adIdViewModel: AdIdViewModel) {
    // Check if root is available
    var isRootAvailable by remember { mutableStateOf(adIdViewModel.isRootAvailable()) }

    if (!isRootAvailable) {
        RootNotAvailable()
        OnResumeEvent { isRootAvailable = adIdViewModel.isRootAvailable() }
    } else {
        val localContext = LocalContext.current
        val navController = rememberNavController()
        val preference by rememberPreferenceBooleanSettingState(
            Constants.periodicResetEnabledKey, false
        )

        LaunchedEffect(Unit) {
            if (preference) AdIdResetWorker.schedule(localContext, false)
        }

        Scaffold(bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                listOf(Screen.Main, Screen.Setting).forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = null,
                                Modifier.padding(3.dp)
                            )
                        },
                        label = { Text(stringResource(screen.resourceId), Modifier.padding(3.dp)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                restoreState = true
                                launchSingleTop = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }
        }) { innerPadding -> AirNavHost(innerPadding, adIdViewModel, navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // this VM generation is not good but this is just a preview anyway.
    val application = LocalContext.current.applicationContext as Application
    val irrelevantViewModel = AdIdViewModel(application)
    ThemedSurface { AdIdResetApp(irrelevantViewModel) }
}