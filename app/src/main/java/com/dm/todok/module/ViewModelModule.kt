package com.dm.todok.module

import com.dm.todok.ui.tasklist.TaskListViewModel
import com.dm.todok.ui.user.UserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        UserViewModel(get())
    }

    viewModel {
        TaskListViewModel(get())
    }
}
