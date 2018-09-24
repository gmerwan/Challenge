package com.wunder.challenge.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class PlaceMark(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val address: String,
        val coordinates: List<Double>,
        val engineType: String,
        val exterior: String,
        val fuel: Int,
        val interior: String,
        val name: String,
        val vin: String
)