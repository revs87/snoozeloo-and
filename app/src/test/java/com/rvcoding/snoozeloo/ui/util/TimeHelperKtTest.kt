package com.rvcoding.snoozeloo.ui.util

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
        assertEquals("1d 17h", timeString)
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
}