package com.nabilbdev.searchflight

import android.app.Application
import com.nabilbdev.searchflight.data.di.AppContainer
import com.nabilbdev.searchflight.data.di.AppDataContainer

class SearchFlightApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}