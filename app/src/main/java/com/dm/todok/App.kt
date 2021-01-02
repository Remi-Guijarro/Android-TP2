package com.dm.todok

import android.app.Application
import com.dm.todok.module.*
import com.dm.todok.network.Api
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@App)
            // modules
            modules(listOf(repoModule, viewModelModule, prefModule, apiModule, taskUseCasesModule))
        }
    }
}