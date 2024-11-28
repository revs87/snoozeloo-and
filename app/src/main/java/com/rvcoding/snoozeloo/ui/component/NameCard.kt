package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsState
import com.rvcoding.snoozeloo.ui.theme.BackgroundCard
import com.rvcoding.snoozeloo.ui.theme.BackgroundCardDark
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.TextSecondary
import com.rvcoding.snoozeloo.ui.theme.TextSecondaryDark
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme

@Composable
fun NameCard(
    state: AlarmSettingsState,
    onAction: (Actions.AlarmSettings) -> Unit
) {
    var name by remember { mutableStateOf(state.alarm.name) }
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.alarm_name),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            TextField(
                value = name,
                onValueChange = {
                    val newName = it.take(30)
                    name = newName
                    onAction.invoke(Actions.AlarmSettings.OnNameChange(newName))
                },
                maxLines = 1,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                ),
                colors = TextFieldDefaults.colors().copy(
                    focusedTextColor = if (isDarkTheme()) PrimaryDark else Primary,
                    unfocusedTextColor = if (isDarkTheme()) TextSecondaryDark else TextSecondary,
                    focusedContainerColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard,
                    unfocusedContainerColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard,
                    focusedPlaceholderColor = if (isDarkTheme()) PrimaryDark else Primary,
                    unfocusedPlaceholderColor = if (isDarkTheme()) TextSecondaryDark else TextSecondary,
                    focusedIndicatorColor = if (isDarkTheme()) PrimaryDark else Primary,
                    unfocusedIndicatorColor = if (isDarkTheme()) TextSecondaryDark else TextSecondary,
                    cursorColor = if (isDarkTheme()) PrimaryDark else Primary,
                    textSelectionColors = TextSelectionColors(
                        handleColor = if (isDarkTheme()) PrimaryDark else Primary,
                        backgroundColor = if (isDarkTheme()) PrimaryDark.copy(alpha = 0.4f) else Primary.copy(alpha = 0.4f)
                    )
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}