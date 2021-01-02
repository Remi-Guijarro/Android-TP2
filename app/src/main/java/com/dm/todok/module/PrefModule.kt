package com.dm.todok.module

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.dm.todok.SHARED_PREF_TOKEN_KEY
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val prefModule = module {
    single { Preferences(androidContext()) }
}

class Preferences(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getToken() = preferences.getString(SHARED_PREF_TOKEN_KEY, "").toString()

    fun setToken(token: String) = preferences.edit {
        putString(SHARED_PREF_TOKEN_KEY, token)
    }
}