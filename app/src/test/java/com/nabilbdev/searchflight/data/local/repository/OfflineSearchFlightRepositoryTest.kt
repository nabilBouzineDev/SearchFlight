package com.nabilbdev.searchflight.data.local.repository

import com.nabilbdev.searchflight.data.local.entity.Airport
import com.nabilbdev.searchflight.data.local.fake.FakeAirportDAO
import com.nabilbdev.searchflight.data.local.fake.FakeDataSource
import com.nabilbdev.searchflight.data.local.fake.FakeFavoriteDAO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


private lateinit var fakeAirportDAO: FakeAirportDAO
private lateinit var fakeFavoriteDAO: FakeFavoriteDAO
private lateinit var offlineSearchFlightRepo: OfflineSearchFlightRepository
private lateinit var fakeAirports: List<Airport>

class OfflineSearchFlightRepositoryTest {

    @Before
    fun setUp() {
        fakeAirportDAO = FakeAirportDAO()
        fakeFavoriteDAO = FakeFavoriteDAO()
        offlineSearchFlightRepo = OfflineSearchFlightRepository(fakeAirportDAO, fakeFavoriteDAO)
        fakeAirports = FakeDataSource.fakeAirports
    }

    // Areas where Repository interact with AirportDAO
    @Test
    fun testGetAirportsByQuery_withMatchingQuery_returnsSingleExpectedAirport() = runBlocking {
        val result = offlineSearchFlightRepo.getAirportsByQueryStream("LAX").first()

        assertTrue(result.size == 1)
        assertEquals(result[0].iataCode, fakeAirports[0].iataCode)
    }

    @Test
    fun testGetAirportsByQuery_withUnmatchedQuery_returnsExpectedAirports() = runBlocking {
        val result = offlineSearchFlightRepo.getAirportsByQueryStream("-%;").first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun testGetAllAirportsExcept_withExcludedAirport_returnsRemainingAirports() = runBlocking {
        val result = offlineSearchFlightRepo.getAllAirportsExceptStream("LAX").first()
        assertTrue(result.none { it.iataCode == fakeAirports[0].iataCode })
    }

    @Test
    fun testGetAirportByCode_withValidCode_returnsMatchingAirport() = runBlocking {
        val result = offlineSearchFlightRepo.getAirportByCodeStream("LAX").first()
        assertEquals(result, fakeAirports[0])
    }

    @Test
    fun testGetAllAirports_returnsCompleteListOfAirports() = runBlocking {
        val result = offlineSearchFlightRepo.getAllAirportsStream().first()
        assertEquals(result.size, fakeAirports.size)
        assertEquals(result[0], fakeAirports[0])
        assertEquals(result[1], fakeAirports[1])
        assertEquals(result[5], fakeAirports[5])
    }

    // Areas where Repository interact with FavoriteDAO
    private suspend fun addOneFavoriteToDB() {
        fakeFavoriteDAO.insert(FakeDataSource.fakeFavoriteAirport1)
    }

    private suspend fun addTwoFavoriteToDB() {
        fakeFavoriteDAO.insert(FakeDataSource.fakeFavoriteAirport1)
        fakeFavoriteDAO.insert(FakeDataSource.fakeFavoriteAirport2)
    }

    @Test
    fun testDeleteFavorite_removesFavoriteSuccessfully() = runBlocking {

        addOneFavoriteToDB()
        offlineSearchFlightRepo.deleteFavoriteAirport(FakeDataSource.fakeFavoriteAirport1)

        val result = offlineSearchFlightRepo.getAllFavoriteAirportsStream().first()
        assertFalse(result.contains(FakeDataSource.fakeFavoriteAirport1))
    }

    @Test
    fun testGetAllFavoriteAirports_returnsCompleteListOfFavoriteAirports() = runBlocking {
        val result = offlineSearchFlightRepo.getAllFavoriteAirportsStream().first()
        assertTrue(result.isEmpty())

        addTwoFavoriteToDB()

        assertEquals(result.size, 2)
        assertEquals(result[0], FakeDataSource.fakeFavoriteAirport1)
        assertEquals(result[1], FakeDataSource.fakeFavoriteAirport2)
    }
}
