package com.rvcoding.snoozeloo.ui.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.rvcoding.snoozeloo.DEEP_LINK_DOMAIN
import com.rvcoding.snoozeloo.common.checkPostNotificationsPermission
import com.rvcoding.snoozeloo.common.checkUseFullScreenPermission
import com.rvcoding.snoozeloo.common.requestUseFullScreenIntent
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
            val context = LocalContext.current

            var hasPCPermission by remember { mutableStateOf(context.checkPostNotificationsPermission()) }
            var permissionPCAnswered by remember { mutableStateOf(false) }
            val permissionPCLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { isGranted ->
                hasPCPermission = isGranted
                permissionPCAnswered = true
            }

            var hasUFSPermission by remember { mutableStateOf(context.checkUseFullScreenPermission()) }
            var permissionUFSAnswered by remember { mutableStateOf(false) }
            val permissionUFSLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                hasUFSPermission = context.checkUseFullScreenPermission()
                permissionUFSAnswered = true
            }

            LaunchedEffect(true) {
                if (!hasPCPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionPCLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    permissionPCAnswered = true
                }

                if (!hasUFSPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    permissionUFSLauncher.launch(context.requestUseFullScreenIntent())
                } else {
                    permissionUFSAnswered = true
                }
            }

            if (permissionPCAnswered && permissionUFSAnswered) {
                YourAlarmsScreenRoot()
            }
        }
        composable<Destination.AlarmSettings> {
            val alarmId = it.toRoute<Destination.AlarmSettings>().id
            AlarmSettingsScreenRoot(alarmId = alarmId)
        }
        composable<Destination.AlarmTrigger>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://$DEEP_LINK_DOMAIN/{id}"
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