package com.nabilbdev.searchflight.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nabilbdev.searchflight.SearchFlightApplication
import com.nabilbdev.searchflight.ui.screens.search.SearchScreenViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire SearchFlight app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchScreenViewModel(
                searchFlightRepository = searchFLightApplication().container.searchFlightRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [SearchFlightApplication].
 */
fun CreationExtras.searchFLightApplication(): SearchFlightApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SearchFlightApplication)