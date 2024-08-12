package com.nabilbdev.searchflight.data.local.fake

import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.entity.Favorite

object FakeDataSource {

    val fakeAirports = listOf(
        Airport(1, "Los Angeles", "LAX", 100),
        Airport(2, "New York", "JFK", 19),
        Airport(3, "Chicago", "ORD", 20),
        Airport(4, "San Francisco", "SFO", 40),
        Airport(5, "Atlanta", "ATL", 599),
        Airport(6, "Paris", "CDG", 30),
        Airport(7, "London", "LHR", 19),
        Airport(8, "Tokyo", "NRT", 201),
        Airport(9, "Sydney", "SYD", 200),
        Airport(10, "Dubai", "DXB", 1999)
    )

    val fakeFavoriteAirport1 = Favorite(0, "Lax", "FAN")
    val fakeFavoriteAirport2 = Favorite(1, "DAM", "BAN")
}