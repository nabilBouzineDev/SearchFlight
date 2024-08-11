package com.nabilbdev.searchflight.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "iata_code") val iataCode: String,
    val passengers: Int,
)
