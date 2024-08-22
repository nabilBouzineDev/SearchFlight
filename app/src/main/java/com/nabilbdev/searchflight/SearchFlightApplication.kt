package com.nabilbdev.searchflight

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nabilbdev.searchflight.data.di.AppContainer
import com.nabilbdev.searchflight.data.di.AppDataContainer
import com.nabilbdev.searchflight.data.local.repository.UserPreferencesRepository


private const val SHOW_FILTER_PREFERENCE_NAME = "show_filters"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SHOW_FILTER_PREFERENCE_NAME
)


class SearchFlightApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}