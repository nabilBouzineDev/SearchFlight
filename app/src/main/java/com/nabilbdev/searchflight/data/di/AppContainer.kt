package com.nabilbdev.searchflight.data.di

import android.content.Context
import com.nabilbdev.searchflight.data.local.database.SearchFlightDatabase
import com.nabilbdev.searchflight.data.local.repository.OfflineSearchFlightRepository
import com.nabilbdev.searchflight.data.local.repository.SearchFlightRepository

interface AppContainer {
    val searchFlightRepository: SearchFlightRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val searchFlightRepository: SearchFlightRepository by lazy {
        OfflineSearchFlightRepository(
            airportDAO = SearchFlightDatabase.getDatabase(context).airportDAO(),
            favoriteDAO = SearchFlightDatabase.getDatabase(context).favoriteDAO()
        )
    }
}