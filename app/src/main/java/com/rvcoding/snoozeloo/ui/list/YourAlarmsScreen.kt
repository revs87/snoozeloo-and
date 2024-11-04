package com.rvcoding.snoozeloo.ui.list

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.ui.component.TopBar
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.TextPrimary
import com.rvcoding.snoozeloo.ui.theme.TextPrimaryDark


@Composable
fun YourAlarmsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(item = TopBar.Title.Default())

    }
}

@Composable
fun YourAlarmsEmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(item = TopBar.Title.Default())
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.padding(bottom = 16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.alarm_blue),
                contentDescription = "Alarm blue",
                tint = if (isSystemInDarkTheme()) PrimaryDark else Primary
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.alarm_list_empty_message),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (isSystemInDarkTheme()) TextPrimaryDark else TextPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenPreview() {
    YourAlarmsScreen()
//    YourAlarmsEmptyScreen()
}