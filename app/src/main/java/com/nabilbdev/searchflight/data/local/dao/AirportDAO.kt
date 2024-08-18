package com.nabilbdev.searchflight.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.nabilbdev.searchflight.data.local.entity.Airport
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDAO {

    // Reminder: Pass the query between the % symbols %query%
    @Query("SELECT * FROM airport WHERE name LIKE :query OR iata_code LIKE :query")
    fun getAirportsByQuery(query: String): Flow<List<Airport>>

    // Exclude the selected airport for departure.
    @Query("SELECT * FROM airport WHERE NOT iata_code = :airportCode ")
    fun getAllAirportsExcept(airportCode: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code = :airportCode ")
    fun getAirportByCode(airportCode: String): Flow<Airport>

    @Query("SELECT * FROM airport")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("SELECT * FROM airport  ORDER BY passengers DESC")
    fun getAllAirportsOrderedByPassengers(): Flow<List<Airport>>
}