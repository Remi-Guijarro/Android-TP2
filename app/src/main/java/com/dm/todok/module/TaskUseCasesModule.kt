package com.dm.todok.module

import com.dm.todok.usecases.TaskInteractor
import com.dm.todok.usecases.TaskUseCases
import org.koin.dsl.module

val taskUseCasesModule = module {
    single<TaskUseCases> { TaskInteractor(get()) }
}