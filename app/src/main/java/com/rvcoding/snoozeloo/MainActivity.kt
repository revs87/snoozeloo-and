package com.rvcoding.snoozeloo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.IS_ALARM_TRIGGERED_EXTRA_KEY
import com.rvcoding.snoozeloo.ui.navigation.NavigationRoot
import com.rvcoding.snoozeloo.ui.theme.BackgroundSurface
import com.rvcoding.snoozeloo.ui.theme.BackgroundSurfaceDark
import com.rvcoding.snoozeloo.ui.theme.SnoozelooTheme
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTriggered = intent?.getBooleanExtra(IS_ALARM_TRIGGERED_EXTRA_KEY, false) == true
        setScreenAsAlarm(isTriggered)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !vm.isReady.value
            }
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = BackgroundSurface.toArgb(),
                darkScrim = BackgroundSurfaceDark.toArgb()
            )
        )
        setContent {
            SnoozelooTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = if (isDarkTheme()) BackgroundSurfaceDark else BackgroundSurface
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavigationRoot()
                        }
                    }
                }
            }
        }
    }

    fun setScreenAsAlarm(isTriggered: Boolean = false) {
        if (isTriggered) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setShowWhenLocked(isTriggered)
        setTurnScreenOn(isTriggered)
    }
}