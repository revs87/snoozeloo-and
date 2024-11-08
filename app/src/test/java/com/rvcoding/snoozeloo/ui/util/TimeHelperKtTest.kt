package com.rvcoding.snoozeloo.ui.util

import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeHelperKtTest {

    @Test
    fun timeAsString_12HourFormat() {
        val timeString = timeAsString(utcTime = 1730738988701, is24HourFormat = false)
        assertEquals("04:49", timeString)
    }

    @Test
    fun timeAsString_24HourFormat() {
        val timeString = timeAsString(utcTime = 1730738988701, is24HourFormat = true)
        assertEquals("16:49", timeString)
    }

    @Test
    fun meridianAsString_AM() {
        val timeString = meridianAsString(utcTime = 1730688988701) // 02:56 AM
        assertEquals("AM", timeString)
    }

    @Test
    fun meridianAsString_PM() {
        val timeString = meridianAsString(utcTime = 1730738988701) // 04:49 PM
        assertEquals("PM", timeString)
    }

    @Test
    fun timeLeftAsString_days() {
        val timeString = timeLeftAsString(
            utcTime = 1730738988701,
            utcNow =  1730588988701
        )
        assertEquals("1d 17h 40min", timeString)
    }

    @Test
    fun timeLeftAsString_hours() {
        val timeString = timeLeftAsString(
            utcTime = 1730738988701, // 16:49
            utcNow =  1730688988701  // 02:56
        )
        assertEquals("13h 53min", timeString)
    }

    @Test
    fun timeLeftAsString_hoursAndZeroSeconds() {
        val timeString = timeLeftAsString(
            utcTime = 1730738968701,
            utcNow =  1730688988701
        )
        assertEquals("13h 53min", timeString)
    }

    @Test
    fun timeLeftAsString_exact5minutes() {
        val timeString = timeLeftAsString(
            utcTime = 1730738988701,
            utcNow =  1730738688701
        )
        assertEquals("5min", timeString)
    }

    @Test
    fun timeLeftAsString_minutes() {
        val timeString = timeLeftAsString(
            utcTime = 1730738988701,
            utcNow =  1730738888701
        )
        assertEquals("1min 40sec", timeString)
    }

    @Test
    fun timeLeftAsString_seconds() {
        val timeString = timeLeftAsString(
            utcTime = 1730738988701,
            utcNow =  1730738985701
        )
        assertEquals("3sec", timeString)
    }

    @Test
    fun timeLeftAsString_negativeTime() {
        val timeString = timeLeftAsString(
            utcTime = 1730688988701,
            utcNow =  1730738988701
        )
        assertEquals("0sec", timeString)
    }

    @Test
    fun nextLocalMidnightInUTC_LocalTimeZone() {
        val actual = nextLocalMidnightInUtc(utcTime = 1730681988701) // Nov 04 00:59:48
        val expected = 1730764800000 // Nov 05 00:00:00
        assertEquals(expected, actual)
    }

    @Test
    fun nextLocalMidnightInUTC_NYTimeZone() {
        val actual = nextLocalMidnightInUtc(
            utcTime = 1730681988701, // Nov 04 00:59:48 UTC
            tz = TimeZone.of("America/New_York") // Nov 03 19:59:48 GMT-5
        )
        val expected = 1730696400000 // Nov 04 00:00:00 @New York
        assertEquals(expected, actual)
    }

    @Test
    fun inHourBoundsOf_from4_to10_returnsTrue() {
        val utcTime = 1730694000000 // Nov 04 04:20:00 UTC
        val actual = utcTime.hourInBounds(
            of = 4,
            and = 10
        )
        assertEquals(true, actual)
    }

    @Test
    fun inHourBoundsOf_from5_to10_returnsFalse() {
        val utcTime = 1730694000000 // Nov 04 04:20:00 UTC
        val actual = utcTime.hourInBounds(
            of = 5,
            and = 10
        )
        assertEquals(false, actual)
    }

    @Test
    fun inHourBoundsOf_from3_to4_returnsFalse() {
        val utcTime = 1730694000000 // Nov 04 04:20:00 UTC
        val actual = utcTime.hourInBounds(
            of = 3,
            and = 4
        )
        assertEquals(false, actual)
    }

    private operator fun <F, S> Pair<F, S>.invoke(block: (customName1: F, customName2: S) -> Unit) = block(first, second)
    @Test
    fun toLocalHoursAnMinutes_12HourFormat() {
        val utcTime = 1730681988701
        val localTime = utcTime.toLocalHoursAnMinutes(is24Hour = false)
        localTime { hours, minutes ->
            assertEquals("12", hours)
            assertEquals("59", minutes)
            assertEquals("AM", meridianAsString(utcTime))
        }
    }

    @Test
    fun toLocalHoursAnMinutes_24HourFormat() {
        val utcTime = 1730681988701
        val localTime = utcTime.toLocalHoursAnMinutes(is24Hour = true)
        localTime { hours, minutes ->
            assertEquals("00", hours)
            assertEquals("59", minutes)
        }
    }
}