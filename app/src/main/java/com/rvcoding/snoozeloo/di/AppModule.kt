package com.rvcoding.snoozeloo.di

import androidx.lifecycle.SavedStateHandle
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.common.StandardDispatchersProvider
import com.rvcoding.snoozeloo.data.AlarmReceiver
import com.rvcoding.snoozeloo.data.AndroidAlarmScheduler
import com.rvcoding.snoozeloo.data.db.AlarmsDatabase
import com.rvcoding.snoozeloo.data.repository.AlarmRepositoryImpl
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.navigation.Destination
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.navigation.DefaultNavigator
import com.rvcoding.snoozeloo.ui.navigation.Navigator
import com.rvcoding.snoozeloo.ui.screen.list.YourAlarmsViewModel
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    /**
     * Setup
     * */
    single<DispatchersProvider> { StandardDispatchersProvider }
    val coExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("CoroutineException: ${throwable.printStackTrace()}")
    }
    single<CoroutineExceptionHandler> { coExceptionHandler }
    single<CoroutineScope> { CoroutineScope(StandardDispatchersProvider.io + coExceptionHandler) }

    /**
     * Navigation
     * */
    single<Navigator> {
        DefaultNavigator(startDestination = Destination.YourAlarms)
    }

    /**
     * Databases
     * */
    single { AlarmsDatabase.createDb(androidContext()).dao }

    /**
     * Repositories
     * */
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
    single<AlarmReceiver> { AlarmReceiver() }
    single<AlarmScheduler> { AndroidAlarmScheduler(get()) }

    /**
     * ViewModels
     * */
    viewModel { YourAlarmsViewModel(get(), get(), get(), get()) }
    viewModel { AlarmSettingsViewModel(get(), get(), get(), get(), SavedStateHandle()) }
}