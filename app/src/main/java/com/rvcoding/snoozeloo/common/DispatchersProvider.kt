package com.rvcoding.snoozeloo.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    val main: CoroutineDispatcher
        get() = Dispatchers.Main
    val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    val default: CoroutineDispatcher
        get() = Dispatchers.Default
    val io: CoroutineDispatcher
        get() = Dispatchers.IO
}
