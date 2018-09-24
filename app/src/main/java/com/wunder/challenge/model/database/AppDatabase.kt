package com.wunder.challenge.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.wunder.challenge.model.CoordinatesConverter
import com.wunder.challenge.model.PlaceMark
import com.wunder.challenge.model.PlaceMarkDao

@Database(entities = arrayOf(PlaceMark::class), version = 1)
@TypeConverters(CoordinatesConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeMarkDao(): PlaceMarkDao
}