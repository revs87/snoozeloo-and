package com.rvcoding.snoozeloo.navigation

sealed interface Actions {
    data object OnAddAlarmButtonClicked : Actions
}