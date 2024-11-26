package com.rvcoding.snoozeloo

import android.app.Application
import com.rvcoding.snoozeloo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
const val DEEP_LINK_DOMAIN = "rvcoding.com"