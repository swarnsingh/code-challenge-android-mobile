package com.swarn.terminalapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TerminalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
