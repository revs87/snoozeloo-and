package com.rvcoding.snoozeloo.ui.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration


private fun timeFormat(is24HourFormat: Boolean): String = when (is24HourFormat) {
    true -> "HH:mm"
    false -> "hh:mm"
}
private fun hoursFormat(is24HourFormat: Boolean): String = when (is24HourFormat) {
    true -> "HH"
    false -> "hh"
}
private fun minutesFormat(): String = "mm"
private fun meridiemFormat(): String = "a"


private fun timeAsStringInternal(utcTime: Long, format: String): String {
    val instant = Instant.fromEpochMilliseconds(utcTime)
    val zonedDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern(format, Locale.UK)
    return formatter.format(zonedDateTime.toJavaLocalDateTime())
}

fun timeAsString(utcTime: Long, is24HourFormat: Boolean): String = timeAsStringInternal(utcTime, timeFormat(is24HourFormat))
fun timeWithMeridiemAsString(utcTime: Long, is24HourFormat: Boolean): String = timeAsStringInternal(utcTime, timeFormat(is24HourFormat)) + if (is24HourFormat) "" else " " + meridianAsString(utcTime)
fun meridianAsString(utcTime: Long): String = timeAsStringInternal(utcTime, meridiemFormat()).uppercase()
fun timeLeftAsString(utcTime: Long, utcNow: Long = System.currentTimeMillis()): String {
    val now = Instant.fromEpochMilliseconds(utcNow) // By utcNow means we're in the past of utcTime
    val instant = Instant.fromEpochMilliseconds(utcTime)
    return durationAsStr(duration = instant - now)
}
private fun durationAsStr(duration: Duration): String {
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

fun Long.nextAlarmTime(utcNow: Long = System.currentTimeMillis()) : Long {
    val alarm = Calendar.getInstance()
    alarm.timeInMillis = this

    val today = Calendar.getInstance()
    today.timeInMillis = if (this > utcNow) this else utcNow

    alarm.set(Calendar.YEAR, today.get(Calendar.YEAR))
    alarm.set(Calendar.MONTH, today.get(Calendar.MONTH))
    alarm.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))

    if (alarm.timeInMillis < utcNow) {
        alarm.add(Calendar.DAY_OF_MONTH, 1)
        return alarm.timeInMillis
    }

    return alarm.timeInMillis
}

fun Long.hourInBounds(of: Int, and: Int): Boolean {
    val localHours = this.toLocalHoursAndMinutes(is24Hour = true).first.toInt()
    return localHours in of until and
}

fun Long.toLocalHoursAndMinutes(is24Hour: Boolean): Pair<String, String> {
    val instant = java.time.Instant.ofEpochMilli(this)
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("${hoursFormat(is24Hour)}:${minutesFormat()}")
    return localDateTime.format(formatter).split(":").let { it[0] to it[1] }
}

fun Pair<String, String>.fromLocalHoursAndMinutes(): Long {
    val (hours, minutes) = this
    val calendar = Calendar.getInstance(java.util.TimeZone.getDefault())
    calendar.set(Calendar.HOUR_OF_DAY, hours.toInt())
    calendar.set(Calendar.MINUTE, minutes.toInt())
    return calendar.timeInMillis
}

fun nextLocalMidnightInUtc(
    utcTime: Long = System.currentTimeMillis(),
    tz: TimeZone = TimeZone.currentSystemDefault()
): Long {
    val now = Instant.fromEpochMilliseconds(utcTime).toLocalDateTime(tz)
    val today = now.date
    val tomorrow = today.plus(1, DateTimeUnit.DAY)
    val midnight = LocalDateTime(tomorrow, LocalTime(0, 0))
    return midnight.toInstant(tz).toEpochMilliseconds()
}

fun stringAsUtcTime(time: String, is24HourFormat: Boolean): Long = 1731738988701