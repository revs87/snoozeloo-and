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
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.ALARM_ID_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.IS_ALARM_TRIGGERED_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.IS_ALARM_TURN_OFF_EXTRA_KEY
import com.rvcoding.snoozeloo.ui.navigation.NavigationRoot
import com.rvcoding.snoozeloo.ui.theme.Background
import com.rvcoding.snoozeloo.ui.theme.BackgroundDark
import com.rvcoding.snoozeloo.ui.theme.SnoozelooTheme
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()
    private val alarmScheduler: AlarmScheduler by inject(AlarmScheduler::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTurnOff = intent?.getBooleanExtra(IS_ALARM_TURN_OFF_EXTRA_KEY, false) == true
        val alarmId = intent?.getIntExtra(ALARM_ID_EXTRA_KEY, -1) ?: -1
        setScreenAsTurnOff(isTurnOff, alarmId)

        val isTriggered = intent?.getBooleanExtra(IS_ALARM_TRIGGERED_EXTRA_KEY, false) == true
        setScreenAsAlarm(isTriggered)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !vm.isReady.value
            }
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Background.toArgb(),
                darkScrim = BackgroundDark.toArgb()
            )
        )
        setContent {
            SnoozelooTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = if (isDarkTheme()) BackgroundDark else Background
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavigationRoot()
                        }
                    }
                }
            }
        }
    }

    private fun setScreenAsTurnOff(isTurnOff: Boolean, alarmId: Int) {
        if (isTurnOff) {
            alarmScheduler.removeNotification(alarmId)
            alarmScheduler.cancel(alarmId)
            alarmScheduler.stopRingtone()
            finish()
        }
    }

    private fun setScreenAsAlarm(isTriggered: Boolean = false) {
        if (isTriggered) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setShowWhenLocked(isTriggered)
        setTurnScreenOn(isTriggered)
    }
}