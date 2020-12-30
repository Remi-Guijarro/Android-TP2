package com.dm.todok

import android.app.Application
import com.dm.todok.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}