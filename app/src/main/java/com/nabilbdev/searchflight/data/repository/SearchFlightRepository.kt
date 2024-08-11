package com.nabilbdev.searchflight.data.repository

import com.nabilbdev.searchflight.data.local.dao.AirportDAO
import com.nabilbdev.searchflight.data.local.dao.FavoriteDAO
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.entity.Favorite
import kotlinx.coroutines.flow.Flow

interface SearchFlightRepository {

    fun getAirportsByQueryStream(query: String): Flow<List<Airport>>

    fun getAllAirportsExceptStream(airportCode: String): Flow<List<Airport>>

    fun getAirportsByCodeStream(airportCode: String): Flow<Airport>

    fun getAllFavoriteAirportsStream(): Flow<List<Favorite>>

    suspend fun insertFavoriteAirport(favorite: Favorite)

    suspend fun deleteFavoriteAirport(favorite: Favorite)
}

class OfflineSearchFlightRepository(
    private val airportDAO: AirportDAO,
    private val favoriteDAO: FavoriteDAO
) : SearchFlightRepository {

    override fun getAirportsByQueryStream(query: String): Flow<List<Airport>> =
        airportDAO.getAirportsByQuery(query)

    override fun getAllAirportsExceptStream(airportCode: String): Flow<List<Airport>> =
        airportDAO.getAllAirportsExcept(airportCode)

    override fun getAirportsByCodeStream(airportCode: String): Flow<Airport> =
        airportDAO.getAirportsByCode(airportCode)

    override fun getAllFavoriteAirportsStream(): Flow<List<Favorite>> =
        favoriteDAO.getAllFavoriteAirports()

    override suspend fun insertFavoriteAirport(favorite: Favorite) =
        favoriteDAO.insert(favorite)

    override suspend fun deleteFavoriteAirport(favorite: Favorite) =
        favoriteDAO.delete(favorite)
}