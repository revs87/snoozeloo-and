package com.rvcoding.snoozeloo.domain.model


sealed interface TimeFormatPreference {
    data object Time24 : TimeFormatPreference
    data object Time12 : TimeFormatPreference

    companion object {
        /**
         * Naive way to declare the default TimeFormatPreference.
         * */
        private fun default() = Time12 as TimeFormatPreference
        fun is24HourFormat(): Boolean = default() is Time24
    }
}