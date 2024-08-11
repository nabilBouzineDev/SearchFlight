package com.nabilbdev.searchflight

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nabilbdev.searchflight.data.local.dao.FavoriteDAO
import com.nabilbdev.searchflight.data.local.database.SearchFlightDatabase
import com.nabilbdev.searchflight.data.local.entity.Favorite
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


private lateinit var searchFlightDatabase: SearchFlightDatabase
private lateinit var favoriteDAO: FavoriteDAO

@RunWith(AndroidJUnit4::class)
class FavoriteDAOTest {

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        searchFlightDatabase =
            Room.inMemoryDatabaseBuilder(context, SearchFlightDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        favoriteDAO = searchFlightDatabase.favoriteDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        searchFlightDatabase.close()
    }


    private var favorite1 = Favorite(1, "OPO", "ARN")
    private var favorite2 = Favorite(2, "TXL", "OSL")

    private suspend fun addOneFavoriteToDB() {
        favoriteDAO.insert(favorite1)
    }

    private suspend fun addTwoFavoriteToDB() {
        favoriteDAO.insert(favorite2)
    }

    @Test
    @Throws(IOException::class)
    fun daoInsert_InsertFavoritesToDB() = runBlocking {
        addOneFavoriteToDB()
        val allItems = favoriteDAO.getAllFavoriteAirports().first()
        Assert.assertEquals(allItems[0], favorite1)
    }
}