@file:OptIn(ExperimentalMaterial3Api::class)

package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerSelectionMode
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.zIndex
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.CornerSmallShape
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.DisplaySeparatorWidth
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.PeriodSelectorVerticalContainerHeight
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.PeriodSelectorVerticalContainerWidth
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.PeriodToggleMargin
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.SeparatorZIndex
import com.rvcoding.snoozeloo.ui.component.ClockDisplayTokens.TimeSelectorContainerHeight
import com.rvcoding.snoozeloo.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale
import java.util.WeakHashMap


internal object ClockDisplayTokens {
    val TimeSelectorContainerHeight = 95.dp

    val DisplaySeparatorWidth = 24.dp
    const val SeparatorZIndex = 2f

    val PeriodSelectorOutlineWidth = 1.0.dp
    val PeriodSelectorVerticalContainerHeight = 78.0.dp
    val PeriodSelectorVerticalContainerWidth = 44.0.dp
    val PeriodToggleMargin = 24.dp

    val CornerSmallShape = RoundedCornerShape(8.dp)
}


@Composable
fun TimeDisplay(
    state: TimePickerState,
    colors: TimePickerColors,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClockDisplayNumbers(
            modifier = Modifier.weight(1f),
            state = state,
            colors = colors,
            onClick = { onClick.invoke() }
        )
        if (!state.is24hour) {
            Box(modifier = Modifier.padding(end = PeriodToggleMargin)) {
                VerticalPeriodToggle(
                    modifier =
                    Modifier.size(
                        PeriodSelectorVerticalContainerWidth,
                        PeriodSelectorVerticalContainerHeight
                    ),
                    state = state,
                    colors = colors,
                )
            }
        }
    }
}

@Composable
private fun ClockDisplayNumbers(
    modifier: Modifier = Modifier,
    state: TimePickerState,
    colors: TimePickerColors,
    onClick: () -> Unit
) {
    val textStyle = MaterialTheme.typography.displayLarge.copy(
        // Adjust properties as needed to match the actual style
        //fontSize = 20.sp
    )
    CompositionLocalProvider(
        LocalTextStyle provides textStyle,
        // Always display the TimeSelectors from left to right.
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeSelector(
                modifier = Modifier
                    .height(TimeSelectorContainerHeight)
                    .weight(1f)
                    .padding(start = PeriodToggleMargin),
                value = state.hourForDisplay,
                state = state,
                selection = TimePickerSelectionMode.Hour,
                colors = colors,
                onClick = { onClick.invoke() }
            )
            DisplaySeparator(
                modifier = Modifier.size(DisplaySeparatorWidth, PeriodSelectorVerticalContainerHeight),
                timeFieldSeparatorColor = TextSecondary
            )
            TimeSelector(
                modifier = Modifier
                    .height(TimeSelectorContainerHeight)
                    .weight(1f)
                    .padding(end = PeriodToggleMargin)
                    .clickable { onClick.invoke() },
                value = state.minute,
                state = state,
                selection = TimePickerSelectionMode.Minute,
                colors = colors,
                onClick = { onClick.invoke() }
            )
        }
    }
}
internal val TimePickerState.hourForDisplay: Int
    get() =
        when {
            is24hour -> hour % 24
            hour % 12 == 0 -> 12
            isAfternoon -> hour - 12
            else -> hour
        }

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimeSelector(
    modifier: Modifier,
    value: Int,
    state: TimePickerState,
    selection: TimePickerSelectionMode,
    colors: TimePickerColors,
    onClick: () -> Unit,
) {
    val selected = state.selection == selection
    val selectorContentDescription = remember {
        if (selection == TimePickerSelectionMode.Hour) {
            "TimePickerHourSelection"
        } else {
            "TimePickerMinuteSelection"
        }
    }

    val containerColor = colors.timeSelectorContainerColor(selected)
    val contentColor = colors.timeSelectorContentColor(selected)
    Surface(
        modifier =
            modifier.semantics(mergeDescendants = true) {
                role = Role.RadioButton
                this.contentDescription = selectorContentDescription
            },
        onClick = {
            if (selection != state.selection) {
                state.selection = selection
            }
            onClick.invoke()
        },
        selected = selected,
        shape = CornerSmallShape,
        color = containerColor,
    ) {
        val valueContentDescription = remember {
            if (selection == TimePickerSelectionMode.Minute) {
                "TimePickerMinuteSuffix"
            } else if (state.is24hour) {
                "TimePicker24HourSuffix"
            } else {
                "TimePickerHourSuffix"
            }
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.semantics { contentDescription = valueContentDescription },
                text = value.toLocalString(minDigits = 2, maxDigits = 2, isGroupingUsed = false),
                color = contentColor,
            )
        }
    }
}
@Stable
internal fun TimePickerColors.timeSelectorContainerColor(selected: Boolean) =
    if (selected) {
        timeSelectorSelectedContainerColor
    } else {
        timeSelectorUnselectedContainerColor
    }
@Stable
internal fun TimePickerColors.timeSelectorContentColor(selected: Boolean) =
    if (selected) {
        timeSelectorSelectedContentColor
    } else {
        timeSelectorUnselectedContentColor
    }
internal fun Int.toLocalString(
    minDigits: Int,
    maxDigits: Int,
    isGroupingUsed: Boolean
): String {
    return getCachedDateTimeFormatter(
        minDigits = minDigits,
        maxDigits = maxDigits,
        isGroupingUsed = isGroupingUsed
    )
        .format(this)
}
private val cachedFormatters = WeakHashMap<String, NumberFormat>()
private fun getCachedDateTimeFormatter(
    minDigits: Int,
    maxDigits: Int,
    isGroupingUsed: Boolean
): NumberFormat {
    // Note: Using Locale.getDefault() as a best effort to obtain a unique key and keeping this
    // function non-composable.
    val key = "$minDigits.$maxDigits.$isGroupingUsed.${Locale.getDefault().toLanguageTag()}"
    return cachedFormatters.getOrPut(key) {
        NumberFormat.getIntegerInstance().apply {
            this.isGroupingUsed = isGroupingUsed
            this.minimumIntegerDigits = minDigits
            this.maximumIntegerDigits = maxDigits
        }
    }
}


@Composable
private fun DisplaySeparator(modifier: Modifier, timeFieldSeparatorColor: Color) {
    val style =
        LocalTextStyle.current.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeightStyle =
                LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
        )

    Box(modifier = modifier.clearAndSetSemantics {}, contentAlignment = Alignment.Center) {
        Text(
            text = ":",
            color = timeFieldSeparatorColor,
            style = style,
        )
    }
}

@Composable
private fun VerticalPeriodToggle(
    modifier: Modifier,
    state: TimePickerState,
    colors: TimePickerColors,
) {
    val measurePolicy = remember {
        MeasurePolicy { measurables, constraints ->
            val spacer = measurables.fastFirst { it.layoutId == "Spacer" }
            val spacerPlaceable =
                spacer.measure(
                    constraints.copy(
                        minHeight = 0,
                        maxHeight = ClockDisplayTokens.PeriodSelectorOutlineWidth.roundToPx()
                    )
                )

            val items =
                measurables
                    .fastFilter { it.layoutId != "Spacer" }
                    .fastMap { item ->
                        item.measure(
                            constraints.copy(minHeight = 0, maxHeight = constraints.maxHeight / 2)
                        )
                    }

            layout(constraints.maxWidth, constraints.maxHeight) {
                items[0].place(0, 0)
                items[1].place(0, items[0].height)
                spacerPlaceable.place(0, items[0].height - spacerPlaceable.height / 2)
            }
        }
    }

    val shape = CornerSmallShape

    PeriodToggleImpl(
        modifier = modifier,
        state = state,
        colors = colors,
        measurePolicy = measurePolicy,
        startShape = shape.top(),
        endShape = shape.bottom()
    )
}
internal fun CornerBasedShape.top(): CornerBasedShape {
    return copy(bottomStart = CornerSize(0.0.dp), bottomEnd = CornerSize(0.0.dp))
}
internal fun CornerBasedShape.bottom(): CornerBasedShape {
    return copy(topStart = CornerSize(0.0.dp), topEnd = CornerSize(0.0.dp))
}



@Composable
private fun PeriodToggleImpl(
    modifier: Modifier,
    state: TimePickerState,
    colors: TimePickerColors,
    measurePolicy: MeasurePolicy,
    startShape: Shape,
    endShape: Shape,
) {
    val borderStroke =
        BorderStroke(ClockDisplayTokens.PeriodSelectorOutlineWidth, colors.periodSelectorBorderColor)

    val shape = CornerSmallShape
    val contentDescription = "TimePickerPeriodToggle"
    Layout(
        modifier = modifier
            .semantics {
                isTraversalGroup = true
                this.contentDescription = contentDescription
            }
            .selectableGroup()
            .border(border = borderStroke, shape = shape),
        measurePolicy = measurePolicy,
        content = {
            ToggleItem(
                checked = !state.isAfternoon,
                shape = startShape,
                onClick = { state.isAfternoon = false },
                colors = colors,
            ) {
                Text(text = "AM")
            }
            Spacer(
                Modifier
                    .layoutId("Spacer")
                    .zIndex(SeparatorZIndex)
                    .fillMaxSize()
                    .background(color = colors.periodSelectorBorderColor)
            )
            ToggleItem(
                checked = state.isAfternoon,
                shape = endShape,
                onClick = { state.isAfternoon = true },
                colors = colors,
            ) {
                Text(text = "PM")
            }
        }
    )
}

@Composable
private fun ToggleItem(
    checked: Boolean,
    shape: Shape,
    onClick: () -> Unit,
    colors: TimePickerColors,
    content: @Composable RowScope.() -> Unit,
) {
    val contentColor = colors.periodSelectorContentColor(checked)
    val containerColor = colors.periodSelectorContainerColor(checked)

    TextButton(
        modifier =
        Modifier
            .zIndex(if (checked) 0f else 1f)
            .fillMaxSize()
            .semantics { selected = checked },
        contentPadding = PaddingValues(0.dp),
        shape = shape,
        onClick = onClick,
        content = content,
        colors =
        ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            containerColor = containerColor
        )
    )
}

@Stable
internal fun TimePickerColors.periodSelectorContentColor(selected: Boolean) =
    if (selected) {
        periodSelectorSelectedContentColor
    } else {
        periodSelectorUnselectedContentColor
    }
@Stable
internal fun TimePickerColors.periodSelectorContainerColor(selected: Boolean) =
    if (selected) {
        periodSelectorSelectedContainerColor
    } else {
        periodSelectorUnselectedContainerColor
    }


