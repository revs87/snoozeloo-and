package com.rvcoding.snoozeloo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rvcoding.snoozeloo.domain.navigation.Destination
import com.rvcoding.snoozeloo.ui.screen.list.YourAlarmsScreenRoot
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsScreenRoot
import org.koin.compose.koinInject


@Composable
fun NavigationRoot(
    navController: NavHostController = rememberNavController()
) {
    val navigator = koinInject<Navigator>()

    ObserveAsEvents(flow = navigator.navigationActions) { action ->
        when(action) {
            is NavigationAction.Navigate -> navController.navigate(
                action.destination
            ) {
                action.navOptions(this)
            }
            NavigationAction.NavigateUp -> navController.navigateUp()
        }
    }

    NavHost(
        navController = navController,
        startDestination = navigator.startDestination
    ) {
        composable<Destination.YourAlarms> {
            YourAlarmsScreenRoot()
        }
        composable<Destination.AlarmSettings> {
            val alarmId = it.toRoute<Destination.AlarmSettings>().id
            AlarmSettingsScreenRoot(alarmId = alarmId)
        }
    }
}