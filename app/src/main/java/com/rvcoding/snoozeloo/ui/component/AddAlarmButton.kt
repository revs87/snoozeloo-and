package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rvcoding.snoozeloo.navigation.Actions
import com.rvcoding.snoozeloo.ui.theme.Primary

@Composable
fun AddAlarmButton(
    modifier: Modifier = Modifier,
    onAction: (Actions) -> Unit = { _ -> }
) {
    FilledIconButton(
        onClick = { onAction.invoke(Actions.OnAddAlarmButtonClicked) },
        modifier = modifier.size(60.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Primary
        )
    ) {
        Icon(
            modifier = Modifier.size(38.dp),
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add alarm button",
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddAlarmButtonPreview() {
    AddAlarmButton()
}