package com.rvcoding.snoozeloo.di

import com.rvcoding.snoozeloo.common.StandardDispatchersProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val appModule = module {
    single { StandardDispatchersProvider }
    val coExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("CoroutineException: ${throwable.printStackTrace()}")
    }
    single { CoroutineScope(StandardDispatchersProvider.io + coExceptionHandler) }

}