package com.rvcoding.snoozeloo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
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
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rvcoding.snoozeloo.ui.navigation.NavigationRoot
import com.rvcoding.snoozeloo.ui.theme.BackgroundSurface
import com.rvcoding.snoozeloo.ui.theme.BackgroundSurfaceDark
import com.rvcoding.snoozeloo.ui.theme.SnoozelooTheme
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !vm.isReady.value
            }
//            setOnExitAnimationListener { screen ->
//                enableEdgeToEdge(
//                    statusBarStyle = SystemBarStyle.auto(
//                        lightScrim = BackgroundSurface.toArgb(),
//                        darkScrim = BackgroundSurfaceDark.toArgb()
//                    )
//                )
//                val zoomX = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.SCALE_X,
//                    0.5f,
//                    0.0f
//                )
//                val zoomY = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.SCALE_Y,
//                    0.5f,
//                    0.0f
//                )
//                zoomX.interpolator = OvershootInterpolator()
//                zoomX.duration = 500L
//                zoomX.doOnEnd { screen.remove() }
//                zoomY.interpolator = OvershootInterpolator()
//                zoomY.duration = 500L
//                zoomY.doOnEnd { screen.remove() }
//
//                zoomX.start()
//                zoomY.start()
//            }
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
}