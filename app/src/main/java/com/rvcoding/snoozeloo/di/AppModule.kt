package com.rvcoding.snoozeloo.di

import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.common.StandardDispatchersProvider
import com.rvcoding.snoozeloo.data.db.AlarmsDatabase
import com.rvcoding.snoozeloo.data.repository.AlarmRepositoryImpl
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.screen.list.YourAlarmsViewModel
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
     * Databases
     * */
    single { AlarmsDatabase.createDb(androidContext()).dao }

    /**
     * Repositories
     * */
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }

    /**
     * ViewModels
     * */
    viewModel { YourAlarmsViewModel(get(), get()) }
}