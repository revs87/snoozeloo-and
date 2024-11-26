package com.rvcoding.snoozeloo.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.rvcoding.snoozeloo.DEEP_LINK_DOMAIN
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
            var hasPermission by remember {
                mutableStateOf(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    } else {
                        true
                    }
                )
            }
            var permissionAnswered by remember { mutableStateOf(false) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { isGranted ->
                hasPermission = isGranted
                permissionAnswered = true
            }

            LaunchedEffect(true) {
                if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    permissionAnswered = true
                }
            }

            if (permissionAnswered) {
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