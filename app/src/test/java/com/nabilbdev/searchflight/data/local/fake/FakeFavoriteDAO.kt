package com.nabilbdev.searchflight.data.local.fake

import com.nabilbdev.searchflight.data.local.dao.FavoriteDAO
import com.nabilbdev.searchflight.data.local.entity.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFavoriteDAO : FavoriteDAO {
    private val favoriteAirports = mutableListOf<Favorite>()

    override suspend fun insert(favorite: Favorite) {
        favoriteAirports.add(favorite)
    }

    override suspend fun delete(favorite: Favorite) {
        favoriteAirports.remove(favorite)
    }

    override fun getAllFavoriteAirports(): Flow<List<Favorite>> = flow {
        emit(favoriteAirports)
    }
}