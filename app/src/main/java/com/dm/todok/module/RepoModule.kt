package com.dm.todok.module

import com.dm.todok.data.TaskRepository
import com.dm.todok.data.TaskRepositoryHttp
import com.dm.todok.data.UserRepository
import com.dm.todok.data.UserRepositoryHttp
import org.koin.dsl.module

val repoModule = module {
    single<UserRepository> { UserRepositoryHttp(get()) }
    single<TaskRepository> { TaskRepositoryHttp(get()) }
}