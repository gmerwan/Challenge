package com.wunder.challenge.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PlaceMarkDao {
    @get:Query("SELECT * FROM placemark")
    val all: List<PlaceMark>

    @Insert
    fun insertAll(vararg placemarks: PlaceMark)
}