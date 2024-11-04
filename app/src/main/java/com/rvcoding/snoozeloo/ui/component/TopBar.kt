package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.ui.theme.BackgroundSurface
import com.rvcoding.snoozeloo.ui.theme.BackgroundSurfaceDark
import com.rvcoding.snoozeloo.ui.theme.GreyDisabled
import com.rvcoding.snoozeloo.ui.theme.GreyDisabledDark
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.TextPrimary
import com.rvcoding.snoozeloo.ui.theme.TextPrimaryDark
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme


@Composable
fun TopBar(item: TopBar) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        when (item) {
            is TopBar.Title -> {
                Text(
                    text = stringResource(item.titleRes),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = item.titleTint,
                    style = LocalTextStyle.current.merge(
                        TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both
                            )
                        )
                    )
                )
            }
            is TopBar.BackNavigator -> TODO()
            is TopBar.Savable -> TODO()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(item = TopBar.Title.Default())
}


sealed interface TopBar {
    data class Title(
        val isDarkTheme: Boolean,
        val titleRes: Int,
        val titleTint: Color = if (isDarkTheme) TextPrimaryDark else TextPrimary,
    ) : TopBar {
        companion object {
            @Composable
            fun Default() = Title(
                isDarkTheme = isDarkTheme(),
                titleRes = R.string.alarm_list_title,
            )
        }
    }

    data class BackNavigator(
        val isDarkTheme: Boolean,
        val onLeftButtonClicked: () -> Unit,
        val leftButtonIconRes: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
        val leftButtonIconTint: Color = if (isDarkTheme) BackgroundSurfaceDark else BackgroundSurface,
        val leftButtonColor: Color = if (isDarkTheme) PrimaryDark else Primary,
    ) : TopBar

    data class Savable(
        val isDarkTheme: Boolean,
        val rightButtonTextRes: Int,
        val rightButtonEnabled: Boolean,
        val onRightButtonClicked: () -> Unit,
        val onLeftButtonClicked: () -> Unit,
        val leftButtonIconRes: ImageVector = Icons.Sharp.Close,
        val leftButtonIconTint: Color = if (isDarkTheme) BackgroundSurfaceDark else BackgroundSurface,
        val leftButtonColor: Color = if (isDarkTheme) GreyDisabledDark else GreyDisabled,
        val rightButtonTextTint: Color = if (isDarkTheme) BackgroundSurfaceDark else BackgroundSurface,
        val rightButtonColor: Color = if (isDarkTheme) PrimaryDark else Primary,
    ) : TopBar
}