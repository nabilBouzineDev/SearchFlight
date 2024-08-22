package com.nabilbdev.searchflight.data.local.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    // Define a key to be able to access the value
    companion object {
        val SHOW_FILTERS = booleanPreferencesKey("show_filters")
        val MOST_VISITED = booleanPreferencesKey("most_visited")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveShowFiltersPreference(showFilters: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_FILTERS] = showFilters
        }
    }

    val showFilters: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SHOW_FILTERS] ?: true
        }
}