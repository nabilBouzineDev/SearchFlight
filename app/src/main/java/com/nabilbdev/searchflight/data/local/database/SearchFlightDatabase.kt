package com.nabilbdev.searchflight.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nabilbdev.searchflight.data.local.dao.AirportDAO
import com.nabilbdev.searchflight.data.local.dao.FavoriteDAO
import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.entity.Favorite

@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = false)
abstract class SearchFlightDatabase : RoomDatabase() {

    abstract fun airportDAO(): AirportDAO
    abstract fun favoriteDAO(): FavoriteDAO

    companion object {

        @Volatile
        private var Instance: SearchFlightDatabase? = null

        fun getDatabase(context: Context): SearchFlightDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SearchFlightDatabase::class.java, "flight_search")
                    .createFromAsset("database/flight_search.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}