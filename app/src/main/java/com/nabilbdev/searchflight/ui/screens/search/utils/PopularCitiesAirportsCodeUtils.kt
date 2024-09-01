package com.nabilbdev.searchflight.ui.screens.search.utils

import com.nabilbdev.searchflight.data.local.entity.Airport

// Used for preview
val AIRPORT_DEFAULT = Airport(1, "No Arrival Selected Yet!", "", 0)

// popular cities code
const val PARIS = "CDG"
const val FRANKFURT = "FRA"
const val AMSTERDAM = "AMS"

const val VIENNA = "VIE"
val popularCitiesAirportCodeList = listOf(PARIS, AMSTERDAM, FRANKFURT, VIENNA)
