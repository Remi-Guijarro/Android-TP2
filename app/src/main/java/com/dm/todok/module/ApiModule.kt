package com.dm.todok.module

import com.dm.todok.network.Api
import org.koin.dsl.module

val apiModule = module {
    single { Api(get()) }
}