package com.dm.todok.module

import com.dm.todok.data.UserRepository
import com.dm.todok.ui.user.UserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userRepoModule = module {
    single { UserRepository() }
}

val viewModelModule = module {
    viewModel {
        UserViewModel(get())
    }
}
