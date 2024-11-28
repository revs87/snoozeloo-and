package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.ui.theme.Background
import com.rvcoding.snoozeloo.ui.theme.BackgroundDark
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
            is TopBar.BackNavigator -> {
                FilledIconButton(
                    onClick = { item.onLeftButtonClicked.invoke() },
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = IconButtonColors(
                        containerColor = item.leftButtonColor,
                        contentColor = item.leftButtonColor,
                        disabledContainerColor = if (item.isDarkTheme) GreyDisabledDark else GreyDisabled,
                        disabledContentColor = if (item.isDarkTheme) GreyDisabledDark else GreyDisabled
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = item.leftButtonIconRes,
                        contentDescription = "Navigate back",
                        tint = item.leftButtonIconTint
                    )
                }
            }
            is TopBar.Savable -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        onClick = { item.onLeftButtonClicked.invoke() },
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = IconButtonColors(
                            containerColor = item.leftButtonColor,
                            contentColor = item.leftButtonColor,
                            disabledContainerColor = if (item.isDarkTheme) GreyDisabledDark else GreyDisabled,
                            disabledContentColor = if (item.isDarkTheme) GreyDisabledDark else GreyDisabled
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = item.leftButtonIconRes,
                            contentDescription = "Navigate back",
                            tint = item.leftButtonIconTint
                        )
                    }
                    TextButton(
                        modifier = Modifier.fillMaxHeight(),
                        enabled = item.rightButtonEnabled,
                        contentPadding = PaddingValues(),
                        colors = ButtonColors(
                            containerColor = item.rightButtonColor,
                            contentColor = item.rightButtonColor,
                            disabledContainerColor = if (item.isDarkTheme) GreyDisabledDark else GreyDisabled,
                            disabledContentColor = if (item.isDarkTheme) GreyDisabledDark else GreyDisabled
                        ),
                        onClick = { item.onRightButtonClicked.invoke() }) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = stringResource(item.rightButtonTextRes),
                            color = item.rightButtonTextTint,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        item = TopBar.Savable(
            isDarkTheme = isDarkTheme(),
            rightButtonEnabled = true,
            onRightButtonClicked = {},
            onLeftButtonClicked = {}
        )
    )
}


sealed interface TopBar {
    data class Title(
        val isDarkTheme: Boolean,
        val titleRes: Int,
        val titleTint: Color = if (isDarkTheme) TextPrimaryDark else TextPrimary,
    ) : TopBar {
        companion object {
            @Composable
            fun alarmList() = Title(
                isDarkTheme = isDarkTheme(),
                titleRes = R.string.alarm_list_title,
            )
        }
    }

    data class BackNavigator(
        val isDarkTheme: Boolean,
        val onLeftButtonClicked: () -> Unit,
        val leftButtonIconRes: ImageVector = Icons.AutoMirrored.Rounded.ArrowBack,
        val leftButtonIconTint: Color = if (isDarkTheme) BackgroundDark else Background,
        val leftButtonColor: Color = if (isDarkTheme) PrimaryDark else Primary,
    ) : TopBar

    data class Savable(
        val isDarkTheme: Boolean,
        val rightButtonEnabled: Boolean,
        val onRightButtonClicked: () -> Unit,
        val onLeftButtonClicked: () -> Unit,
        val rightButtonTextRes: Int = R.string.save,
        val leftButtonIconRes: ImageVector = Icons.Rounded.Close,
        val leftButtonIconTint: Color = if (isDarkTheme) BackgroundDark else Background,
        val leftButtonColor: Color = if (isDarkTheme) PrimaryDark else Primary,
        val rightButtonTextTint: Color = if (isDarkTheme) BackgroundDark else Background,
        val rightButtonColor: Color = if (isDarkTheme) PrimaryDark else Primary,
    ) : TopBar
}