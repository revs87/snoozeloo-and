package com.rvcoding.snoozeloo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.rvcoding.snoozeloo.domain.navigation.Destination
import com.rvcoding.snoozeloo.ui.screen.list.YourAlarmsScreenRoot
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsScreenRoot
import com.rvcoding.snoozeloo.ui.screen.trigger.AlarmTriggerScreenRoot
import org.koin.compose.koinInject
import kotlin.reflect.typeOf


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
        composable<Destination.AlarmTrigger>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://rvcoding.com/{id}"
                }
            ),
            typeMap = mapOf(
                typeOf<Int>() to NavType.IntType
            )
        ) {
            val alarmId = it.toRoute<Destination.AlarmTrigger>().id
            AlarmTriggerScreenRoot(alarmId = alarmId)
        }
    }
}