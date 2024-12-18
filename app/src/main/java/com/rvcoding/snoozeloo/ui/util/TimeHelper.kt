package com.rvcoding.snoozeloo.ui.util

import com.rvcoding.snoozeloo.domain.model.TimeFormatPreference
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


private fun dateFormat(): String = "dd/MM"
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

fun timeAsString(utcTime: Long, is24HourFormat: Boolean = TimeFormatPreference.is24HourFormat()): String = timeAsStringInternal(utcTime, timeFormat(is24HourFormat))
fun timeWithMeridiemAsString(utcTime: Long, is24HourFormat: Boolean = TimeFormatPreference.is24HourFormat()): String = timeAsStringInternal(utcTime, timeFormat(is24HourFormat)) + if (is24HourFormat) "" else " " + meridianAsString(utcTime)
fun timeWithMeridiemAndDateAsString(utcTime: Long, is24HourFormat: Boolean = TimeFormatPreference.is24HourFormat()): String = timeAsStringInternal(utcTime, timeFormat(is24HourFormat)) + if (is24HourFormat) "" else " " + meridianAsString(utcTime) + " (${timeAsStringInternal(utcTime, dateFormat())})"
fun meridianAsString(utcTime: Long): String = timeAsStringInternal(utcTime, meridiemFormat()).uppercase()
fun timeLeftAsString(utcTime: Long, utcNow: Long = System.currentTimeMillis()): String {
    val instant = Instant.fromEpochMilliseconds(utcTime.futureTime(utcNow))
    val now = Instant.fromEpochMilliseconds(utcNow.truncateToMinute()) // By utcNow means we're in the past of utcTime
    return durationAsStr(duration = instant - now)
}
private fun durationAsStr(duration: Duration): String {
    return if (duration.isNegative()) {
        "0sec"
    } else if (duration.inWholeSeconds == 0L) {
        "1d"
    } else {
        val days = duration.inWholeDays
        val hours = duration.inWholeHours % 24
        val minutes = duration.inWholeMinutes % 60

        buildString {
            if (days > 0) append("${days}d ")
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}min ")
        }.trim()
    }
}

fun Long.futureTime(utcNow: Long = System.currentTimeMillis()) : Long {
    val alarm = Calendar.getInstance()
    alarm.timeInMillis = this

    val today = Calendar.getInstance()
    today.timeInMillis = if (this > utcNow) this else utcNow
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)

    alarm.set(Calendar.YEAR, today.get(Calendar.YEAR))
    alarm.set(Calendar.MONTH, today.get(Calendar.MONTH))
    alarm.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))
    alarm.set(Calendar.SECOND, 0)
    alarm.set(Calendar.MILLISECOND, 0)

    if (alarm.timeInMillis < today.timeInMillis) {
        alarm.add(Calendar.DAY_OF_MONTH, 1)
        return alarm.timeInMillis
    }

    return alarm.timeInMillis
}

fun Long.isLessThanAMinute(utcNow: Long = System.currentTimeMillis()): Boolean {
    val alarm = Calendar.getInstance()
    alarm.timeInMillis = this.truncateToMinute()

    val now = Calendar.getInstance()
    now.timeInMillis = utcNow

    return (now.get(Calendar.YEAR) == alarm.get(Calendar.YEAR)
            && now.get(Calendar.MONTH) == alarm.get(Calendar.MONTH)
            && now.get(Calendar.DAY_OF_MONTH) == alarm.get(Calendar.DAY_OF_MONTH)
            && now.get(Calendar.HOUR_OF_DAY) == alarm.get(Calendar.HOUR_OF_DAY)
            && now.get(Calendar.MINUTE) + 1 == alarm.get(Calendar.MINUTE))
}

fun Long.hourInBounds(of: Int, and: Int): Boolean {
    val localHours = this.toLocalHoursAndMinutes(is24Hour = true).first.toInt()
    return localHours in of until and
}

fun Long.toLocalHoursAndMinutes(is24Hour: Boolean): Pair<String, String> {
    val instant = java.time.Instant.ofEpochMilli(this.truncateToMinute())
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("${hoursFormat(is24Hour)}:${minutesFormat()}")
    return localDateTime.format(formatter).split(":").let { it[0] to it[1] }
}

fun Triple<String, String, Boolean>.fromLocalHoursAndMinutes24Format(): Long {
    val (hours, minutes, isPM) = this
    val calendar = Calendar.getInstance(java.util.TimeZone.getDefault())
    calendar.set(Calendar.HOUR_OF_DAY, hours.toInt())
    println("[TimeContent] Calendar: ${calendar.get(Calendar.HOUR_OF_DAY)}")
    calendar.set(Calendar.MINUTE, minutes.toInt())
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.AM_PM, if (isPM) Calendar.PM else Calendar.AM)
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

fun Long.truncateToMinute(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}