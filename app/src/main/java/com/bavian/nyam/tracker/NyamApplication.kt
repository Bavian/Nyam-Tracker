package com.bavian.nyam.tracker

import android.app.Application
import com.bavian.nyam.tracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NyamApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@NyamApplication)
            modules(appModule)
        }
    }
}
