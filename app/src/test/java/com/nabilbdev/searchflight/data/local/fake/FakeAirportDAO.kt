package com.nabilbdev.searchflight.data.local.fake

import com.nabilbdev.searchflight.data.local.dao.AirportDAO
import com.nabilbdev.searchflight.data.local.entity.Airport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

class FakeAirportDAO : AirportDAO {
    private val airports = FakeDataSource.fakeAirports

    /**
     * We mimic the same SQLITE query behaviour exist in the [AirportDAO].
     *
     * f.e: LIKE %query% changed to it.name.contains(query)
     */
    override fun getAirportsByQuery(query: String): Flow<List<Airport>> = flow {

        emit(
            airports.filter {
                it.name.contains(query, ignoreCase = true) || it.iataCode.contains(query)
            }
        )
    }

    override fun getAllAirportsExcept(airportCode: String): Flow<List<Airport>> = flow {
        emit(airports.filter { it.iataCode != airportCode })
    }

    override fun getAirportByCode(airportCode: String): Flow<Airport> = flow {
        airports.find { it.iataCode == airportCode }?.let { emit(it) }
    }.filterNotNull()

    override fun getAllAirports(): Flow<List<Airport>> = flow {
        emit(airports)
    }

    override fun getAllAirportsOrderedByPassengers(): Flow<List<Airport>>  = flow {
        emit(airports.sortedByDescending { it.passengers }.take(4))
    }
}
