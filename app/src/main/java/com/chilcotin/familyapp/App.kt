package com.chilcotin.familyapp

import android.app.Application
import com.chilcotin.familyapp.di.MainModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    val database by lazy {
        MainModule.provideMainDb(this)
    }
}