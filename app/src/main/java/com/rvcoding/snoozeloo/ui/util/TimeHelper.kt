package com.rvcoding.snoozeloo.ui.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


private fun getTimeFormat(is24HourFormat: Boolean): String = when (is24HourFormat) {
    true -> "HH:mm"
    false -> "hh:mm"
}
private fun getMeridianFormat(): String = "a"

private fun timeAsStringInternal(utcTime: Long, format: String): String {
    val instant = Instant.fromEpochMilliseconds(utcTime)
    val zonedDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern(format, Locale.UK)
    return formatter.format(zonedDateTime.toJavaLocalDateTime())
}

fun timeAsString(utcTime: Long, is24HourFormat: Boolean): String = timeAsStringInternal(utcTime, getTimeFormat(is24HourFormat))
fun meridianAsString(utcTime: Long): String = timeAsStringInternal(utcTime, getMeridianFormat()).uppercase()
fun timeLeftAsString(utcTime: Long, utcNow: Long = System.currentTimeMillis()): String {
    val now = Instant.fromEpochMilliseconds(utcNow) // By utcNow means we're in the past of utcTime
    val instant = Instant.fromEpochMilliseconds(utcTime)
    val duration = instant - now

    return if (duration.isNegative()) {
        "0sec"
    } else {
        val days = duration.inWholeDays
        val hours = duration.inWholeHours % 24
        val minutes = duration.inWholeMinutes % 60
        val seconds = duration.inWholeSeconds % 60

        buildString {
            if (days > 0) append("${days}d ")
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}min ")
            if (days == 0L && hours == 0L && seconds > 0L) append("${seconds}sec")
        }.trim()
    }
}

fun nextLocalMidnightInUTC(): Long {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val today = now.date
    val tomorrow = today.plus(1, DateTimeUnit.DAY)
    val midnight = LocalDateTime(tomorrow, LocalTime(0, 0))
    return midnight.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun stringAsUtcTime(time: String, is24HourFormat: Boolean): Long = 1731738988701