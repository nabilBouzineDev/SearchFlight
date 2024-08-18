package data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nabilbdev.searchflight.data.local.dao.AirportDAO
import com.nabilbdev.searchflight.data.local.database.SearchFlightDatabase
import com.nabilbdev.searchflight.data.local.entity.Airport
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


private lateinit var airportDAO: AirportDAO
private lateinit var searchFlightDatabase: SearchFlightDatabase

// Sample data from the actual database
private val airport1 = Airport(
    1,
    "Francisco Sá Carneiro Airport",
    "OPO",
    5053134
)
private val airport2 = Airport(
    2,
    "Stockholm Arlanda Airport",
    "ARN",
    7494765
)

// Sample sorted data by passengers from the actual database to test sorting
private val airport3 = Airport(
    28,
    "Munich International Airport",
    "MUC",
    47959885
)
private val airport4 = Airport(
    7,
    "Sheremetyevo - A.S. Pushkin international airport",
    "SVO",
    49933000
)

@RunWith(AndroidJUnit4::class)
class AirportDAOTest {

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        searchFlightDatabase =
            Room.databaseBuilder(context, SearchFlightDatabase::class.java, "flight_search_test")
                .createFromAsset("database/flight_search.db")
                .allowMainThreadQueries() // Only for testing purposes
                .build()

        airportDAO = searchFlightDatabase.airportDAO()

    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        searchFlightDatabase.close()
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAllAirports_returnAllAirports() = runBlocking {
        val allAirports = airportDAO.getAllAirports().first()

        assertTrue(allAirports.isNotEmpty())
        assertEquals(allAirports[0], airport1)
        assertEquals(allAirports[1], airport2)
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAllAirportsOrderedByPassengers_returnAllAirportsWithCorrectSorting() = runBlocking {
        val allAirports = airportDAO.getAllAirportsOrderedByPassengers().first()

        assertTrue(allAirports.isNotEmpty())

        assertTrue(airport3.passengers < airport4.passengers) // True So airport4 must be first
        assertEquals(allAirports[0], airport4)
        assertEquals(allAirports[1], airport3)
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAirportByCode_returnSingleAirport() = runBlocking {
        val airportResultOne = airportDAO.getAirportByCode("OPO")
        val airportResultTwo = airportDAO.getAirportByCode("ARN")

        assertEquals(airportResultOne.first(), airport1)
        assertEquals(airportResultTwo.first(), airport2)
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAirportExcept_verifyNotExistInReturnedAirports() = runBlocking {
        val allAirports = airportDAO.getAllAirportsExcept("OPO").first()

        assertTrue(airport1 !in allAirports) // airport1 is not in allAirports
        assertTrue(airport2 in allAirports)
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAirportByQuery_verifyNotReturnAllResultsFromDB() = runBlocking {
        val query = "FR"
        val allAirportsByQuery = airportDAO.getAirportsByQuery("%$query%").first()
        val allAirports = airportDAO.getAllAirports().first()

        assertTrue(allAirports.size > allAirportsByQuery.size)
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAirportByQuery_verifyReturnedAirportsIncludeQuery() = runBlocking {
        val query = "FR"
        val allAirportsByQuery = airportDAO.getAirportsByQuery("%$query%").first()

        assertTrue(allAirportsByQuery[0].name.contains(query, ignoreCase = true))
        assertTrue(allAirportsByQuery[1].name.contains(query, ignoreCase = true))
    }
}